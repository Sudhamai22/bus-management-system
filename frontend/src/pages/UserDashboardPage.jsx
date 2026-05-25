import { useAuth } from "../context/AuthContext";

export default function UserDashboardPage() {
  const { user, logout } = useAuth();

  return (
    <main className="dash-shell">
      <section className="dash-card">
        <h1>User Dashboard</h1>
        <p>Welcome {user?.name || "User"}. You are authenticated with JWT.</p>
        <p>
          <strong>Role:</strong> {user?.role || "USER"}
        </p>
        <button className="logout-btn" onClick={logout}>
          Logout
        </button>
      </section>
    </main>
  );
}
