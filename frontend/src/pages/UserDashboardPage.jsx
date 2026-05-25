import { useMemo, useState, useEffect } from "react";
import { useAuth } from "../context/AuthContext";
import { getMyBookings, cancelBooking, createBooking } from "../services/userApi";
import { getRoutes } from "../services/adminApi";

export default function UserDashboardPage() {
  const { user, logout } = useAuth();

  const [searchForm, setSearchForm] = useState({
    source: "",
    destination: "",
    date: "",
    time: "",
  });
  const [searchDone, setSearchDone] = useState(false);
  const [selectedBusId, setSelectedBusId] = useState("");
  const [selectedSeats, setSelectedSeats] = useState([]);
  const [paymentData, setPaymentData] = useState({
    cardName: "",
    cardNumber: "",
    expiry: "",
    cvv: "",
  });
  const [paymentSuccess, setPaymentSuccess] = useState(false);
  const [bookings, setBookings] = useState([]);
  const [paymentError, setPaymentError] = useState("");
  const [paymentProcessing, setPaymentProcessing] = useState(false);

  const [availableBuses, setAvailableBuses] = useState([]);
  const selectedBus = useMemo(
    () => availableBuses.find((bus) => bus.id === selectedBusId) || null,
    [selectedBusId, availableBuses]
  );

  const totalFare = selectedBus ? selectedBus.fare * selectedSeats.length : 0;

  function handleSearchChange(event) {
    const { name, value } = event.target;
    setSearchForm((prev) => {
      const newForm = { ...prev, [name]: value };
      // if both source and destination are set, do a quick search
      if (newForm.source && newForm.destination) {
        quickSearch(newForm);
      }
      return newForm;
    });
  }

  function quickSearch(form) {
    const src = form.source.trim().toLowerCase();
    const dst = form.destination.trim().toLowerCase();
    const date = form.date;
    const time = form.time;

    getRoutes()
      .then((routes) => {
        const results = (routes || [])
          .map((r) => {
            const bus = r.bus || {};
            let routeDate = r.travelDate || "";
            let depTime = "";
            let arrTime = "";
            if (!routeDate && r.departureTime) {
              const d = new Date(r.departureTime);
              routeDate = d.toISOString().slice(0, 10);
              depTime = d.toTimeString().slice(0, 5);
            } else if (r.departureTime) {
              const d = new Date(r.departureTime);
              depTime = d.toTimeString().slice(0, 5);
            }
            if (r.arrivalTime) {
              const a = new Date(r.arrivalTime);
              arrTime = a.toTimeString().slice(0, 5);
            }

            return {
              id: r.routeId,
              operator: bus.busName || bus.busNumber || "Operator",
              source: r.source,
              destination: r.destination,
              date: routeDate,
              time: depTime,
              arrival: arrTime,
              fare: r.fare,
              seatsAvailable: bus.totalSeats || 0,
              raw: r,
            };
          })
          .filter((bus) => {
            const sourceMatch = bus.source.toLowerCase().includes(src);
            const destinationMatch = bus.destination.toLowerCase().includes(dst);
            const dateMatch = date ? bus.date === date : true;
            const timeMatch = time ? (bus.time || "") >= time : true;
            return sourceMatch && destinationMatch && dateMatch && timeMatch;
          });

        setAvailableBuses(results);
        setSearchDone(true);
      })
      .catch(() => setAvailableBuses([]));
  }

  function handleSearchSubmit(event) {
    event.preventDefault();
    setSearchDone(true);
    setSelectedBusId("");
    setSelectedSeats([]);
    setPaymentSuccess(false);
    // fetch routes from backend and map to available buses
    const src = searchForm.source.trim().toLowerCase();
    const dst = searchForm.destination.trim().toLowerCase();
    const date = searchForm.date;
    const time = searchForm.time;

    getRoutes()
      .then((routes) => {
        const results = (routes || [])
          .map((r) => {
            const bus = r.bus || {};
            // derive date/time from travelDate or departureTime
            let routeDate = r.travelDate || "";
            let depTime = "";
            let arrTime = "";
            if (!routeDate && r.departureTime) {
              const d = new Date(r.departureTime);
              routeDate = d.toISOString().slice(0, 10);
              depTime = d.toTimeString().slice(0, 5);
            } else if (r.departureTime) {
              const d = new Date(r.departureTime);
              depTime = d.toTimeString().slice(0, 5);
            }
            if (r.arrivalTime) {
              const a = new Date(r.arrivalTime);
              arrTime = a.toTimeString().slice(0, 5);
            }

            return {
              id: r.routeId,
              operator: bus.busName || bus.busNumber || "Operator",
              source: r.source,
              destination: r.destination,
              date: routeDate,
              time: depTime,
              arrival: arrTime,
              fare: r.fare,
              seatsAvailable: bus.totalSeats || 0,
              raw: r,
            };
          })
          .filter((bus) => {
            const sourceMatch = bus.source.toLowerCase().includes(src);
            const destinationMatch = bus.destination.toLowerCase().includes(dst);
            const dateMatch = date ? bus.date === date : true;
            const timeMatch = time ? (bus.time || "") >= time : true;
            return sourceMatch && destinationMatch && dateMatch && timeMatch;
          });

        setAvailableBuses(results);
      })
      .catch(() => {
        setAvailableBuses([]);
      });
  }

  async function handlePayment(event) {
    event.preventDefault();
    if (!selectedBus) return;
    setPaymentError("");
    // client-side validation
    const cardDigits = String(paymentData.cardNumber).replace(/\D/g, "");
    if (cardDigits.length !== 16) {
      setPaymentError("Card number must be 16 digits.");
      return;
    }
    if (!/^\d{2}\/\d{2}$/.test(paymentData.expiry)) {
      setPaymentError("Expiry must be in MM/YY format.");
      return;
    }
    if (!/^\d{3}$/.test(paymentData.cvv)) {
      setPaymentError("CVV must be 3 digits.");
      return;
    }

    const payload = {
      route: { routeId: selectedBus.id },
      seats: selectedSeats,
      totalAmount: totalFare,
      payment: {
        cardHolder: paymentData.cardName,
        cardNumber: cardDigits,
        expiry: paymentData.expiry,
      },
    };

    try {
      setPaymentProcessing(true);
      const res = await createBooking(payload);
      setPaymentSuccess(true);
      setPaymentData({ cardName: "", cardNumber: "", expiry: "", cvv: "" });
      await loadBookings();
    } catch (err) {
      console.error("Payment error:", err);
      setPaymentError(err?.response?.data?.message || err.message || "Payment failed");
      setPaymentSuccess(false);
    } finally {
      setPaymentProcessing(false);
    }
  }

  function loadBookings() {
    getMyBookings()
      .then((data) => {
        const items = (data || []).map((b) => ({
          id: b.bookingId || b.id,
          bus: b.route?.bus || null,
          route: b.route || null,
          seats: b.seats || [],
          amount: b.totalAmount || b.amount || 0,
          status: b.status || "",
          createdAt: b.bookingDate || b.createdAt || b.date,
        }));
        setBookings(items);
      })
      .catch(() => setBookings([]));
  }

  useEffect(() => {
    loadBookings();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  function handleCancel(bookingId) {
    if (!confirm("Cancel this booking?")) return;
    cancelBooking(bookingId).then(() => loadBookings());
  }

  return (
    <main className="user-shell">
      <section className="user-card">
        <header className="user-header">
          <div>
            <h1>User Dashboard</h1>
            <p>
              Welcome {user?.name || "User"}. Search buses, select seats, and
              complete payment.
            </p>
          </div>
          <button className="logout-btn" onClick={logout}>
            Logout
          </button>
        </header>

        <section className="panel-block">
          <h2>1. Search Buses</h2>
          <form className="search-grid" onSubmit={handleSearchSubmit}>
            <label htmlFor="source">Source</label>
            <input
              id="source"
              name="source"
              type="text"
              placeholder="e.g. Chennai"
              value={searchForm.source}
              onChange={handleSearchChange}
              required
            />

            <label htmlFor="destination">Destination</label>
            <input
              id="destination"
              name="destination"
              type="text"
              placeholder="e.g. Bangalore"
              value={searchForm.destination}
              onChange={handleSearchChange}
              required
            />

            <label htmlFor="date">Date</label>
            <input
              id="date"
              name="date"
              type="date"
              value={searchForm.date}
              onChange={handleSearchChange}
              required
            />

            <label htmlFor="time">Time</label>
            <input
              id="time"
              name="time"
              type="time"
              value={searchForm.time}
              onChange={handleSearchChange}
              required
            />

            <button type="submit" className="primary-btn">
              Search Available Buses
            </button>
            <button type="button" className="ghost-btn" onClick={() => {
              // show all upcoming buses
              getRoutes()
                .then((routes) => {
                  const results = (routes || []).map((r) => {
                    const bus = r.bus || {};
                    let routeDate = r.travelDate || "";
                    let depTime = "";
                    let arrTime = "";
                    if (!routeDate && r.departureTime) {
                      const d = new Date(r.departureTime);
                      routeDate = d.toISOString().slice(0, 10);
                      depTime = d.toTimeString().slice(0, 5);
                    } else if (r.departureTime) {
                      const d = new Date(r.departureTime);
                      depTime = d.toTimeString().slice(0, 5);
                    }
                    if (r.arrivalTime) {
                      const a = new Date(r.arrivalTime);
                      arrTime = a.toTimeString().slice(0, 5);
                    }
                    return {
                      id: r.routeId,
                      operator: bus.busName || bus.busNumber || "Operator",
                      source: r.source,
                      destination: r.destination,
                      date: routeDate,
                      time: depTime,
                      arrival: arrTime,
                      fare: r.fare,
                      seatsAvailable: bus.totalSeats || 0,
                      raw: r,
                    };
                  });
                  setAvailableBuses(results);
                  setSearchDone(true);
                })
                .catch(() => setAvailableBuses([]));
            }}>Show all upcoming buses</button>
          </form>
        </section>

        <section className="panel-block">
          <h2>2. Available Buses</h2>
          {!searchDone ? <p className="muted-text">Search to view buses.</p> : null}
          {searchDone && availableBuses.length === 0 ? (
            <p className="muted-text">No buses found for the selected criteria.</p>
          ) : null}

          <div className="bus-list">
            {availableBuses.map((bus) => (
              <article className="bus-item" key={bus.id}>
                <div>
                  <h3>
                    {bus.operator} ({bus.id})
                  </h3>
                  <p>
                    {bus.source} to {bus.destination} | {bus.date} | {bus.time} to {" "}
                    {bus.arrival}
                  </p>
                  <p>
                    Fare: Rs. {bus.fare} | Seats Available: {bus.seatsAvailable}
                  </p>
                </div>
                <button
                  type="button"
                  className={selectedBusId === bus.id ? "secondary-btn selected" : "secondary-btn"}
                  onClick={() => handleSelectBus(bus.id)}
                >
                  {selectedBusId === bus.id ? "Selected" : "Select Bus"}
                </button>
              </article>
            ))}
          </div>
        </section>

        {selectedBus ? (
          <section className="panel-block">
            <h2>3. Select Seats</h2>
            <p className="muted-text">Choose up to 6 seats.</p>
            <div className="seat-grid">
              {Array.from({ length: 24 }, (_, index) => index + 1).map((seatNo) => (
                <button
                  key={seatNo}
                  type="button"
                  className={selectedSeats.includes(seatNo) ? "seat-btn active" : "seat-btn"}
                  onClick={() => toggleSeat(seatNo)}
                >
                  {seatNo}
                </button>
              ))}
            </div>
          </section>
        ) : null}

        {selectedBus && selectedSeats.length > 0 ? (
          <section className="panel-block">
            <h2>4. Booking & Payment</h2>
            <div className="summary-box">
              <p>
                <strong>Bus:</strong> {selectedBus.operator} ({selectedBus.id})
              </p>
              <p>
                <strong>Route:</strong> {selectedBus.source} to {selectedBus.destination}
              </p>
              <p>
                <strong>Seats:</strong> {selectedSeats.join(", ")}
              </p>
              <p>
                <strong>Total Amount:</strong> Rs. {totalFare}
              </p>
            </div>

            <form className="payment-grid" onSubmit={handlePayment}>
              <label htmlFor="cardName">Card Holder Name</label>
              <input
                id="cardName"
                name="cardName"
                type="text"
                value={paymentData.cardName}
                onChange={handlePaymentChange}
                required
              />

              <label htmlFor="cardNumber">Card Number</label>
              <input
                id="cardNumber"
                name="cardNumber"
                type="text"
                maxLength={19} /* 16 digits + 3 spaces when grouped */
                value={paymentData.cardNumber}
                onChange={handlePaymentChange}
                required
              />
              <div aria-live="polite">
                {String(paymentData.cardNumber).replace(/\D/g, "").length === 16 ? (
                  <small style={{ color: "#1f6a49", fontWeight: 700 }}>Card number valid</small>
                ) : (
                  <small style={{ color: "#b33939", fontWeight: 700 }}>Card number must be 16 digits</small>
                )}
              </div>

              <label htmlFor="expiry">Expiry (MM/YY)</label>
              <input
                id="expiry"
                name="expiry"
                type="text"
                placeholder="MM/YY"
                pattern="(0[1-9]|1[0-2])/[0-9]{2}"
                maxLength={5}
                value={paymentData.expiry}
                onChange={handlePaymentChange}
                required
              />

              <label htmlFor="cvv">CVV</label>
              <input
                id="cvv"
                name="cvv"
                type="password"
                pattern="[0-9]{3}"
                maxLength={3}
                value={paymentData.cvv}
                onChange={handlePaymentChange}
                required
              />

              <button type="submit" className="primary-btn">
                Pay Rs. {totalFare}
              </button>
            </form>

            {paymentError ? <div className="admin-alert admin-alert-error" style={{ marginTop: 10 }}>{paymentError}</div> : null}

            {paymentSuccess ? (
              <div className="success-note" role="status" aria-live="polite">
                Payment done successfully. Your booking is confirmed.
              </div>
            ) : null}
          </section>
        ) : null}

          <section className="panel-block">
            <h2>My Bookings</h2>
            {bookings.length === 0 ? (
              <p className="muted-text">You have no bookings yet.</p>
            ) : (
              <table className="table">
                <thead>
                  <tr>
                    <th>Booking ID</th>
                    <th>Bus</th>
                    <th>Route</th>
                    <th>Seats</th>
                    <th>Amount</th>
                    <th>Status</th>
                    <th>Date</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {bookings.map((b) => (
                    <tr key={b.id}>
                      <td>{b.id}</td>
                      <td>{b.bus?.operator || b.busId}</td>
                      <td>
                        {b.route?.source || ""} to {b.route?.destination || ""}
                      </td>
                      <td>{Array.isArray(b.seats) ? b.seats.join(", ") : b.seats}</td>
                      <td>Rs. {b.amount}</td>
                      <td>{b.status || "-"}</td>
                      <td>{new Date(b.createdAt || b.date || Date.now()).toLocaleString()}</td>
                      <td>
                        <button className="secondary-btn" onClick={() => handleCancel(b.id)}>
                          Cancel
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </section>
      </section>
    </main>
  );
}
