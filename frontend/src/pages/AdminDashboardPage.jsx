import { useAuth } from "../context/AuthContext";

export default function AdminDashboardPage() {
  const { user, logout } = useAuth();

  return (
    <main className="dash-shell">
      <section className="dash-card">
        <h1>Admin Dashboard</h1>
        <p>Welcome {user?.name || "Admin"}. This route is ADMIN-only.</p>
        <p>
          <strong>Role:</strong> {user?.role || "ADMIN"}
        </p>
        <button className="logout-btn" onClick={logout}>
          Logout
        </button>
      </section>
    </main>
  );
}
