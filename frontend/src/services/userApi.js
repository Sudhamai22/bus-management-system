import api from "./authApi";

export const getMyBookings = () => api.get("/bookings").then((res) => res.data);
export const createBooking = (payload) => api.post("/bookings/book", payload).then((res) => res.data);
export const cancelBooking = (id) => api.delete(`/bookings/cancel/${id}`).then((res) => res.data);

export default {
  getMyBookings,
  createBooking,
  cancelBooking,
};
