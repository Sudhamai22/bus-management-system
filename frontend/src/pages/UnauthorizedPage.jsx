import { Link } from "react-router-dom";

export default function UnauthorizedPage() {
  return (
    <main className="auth-shell">
      <section className="auth-card">
        <h1>Unauthorized</h1>
        <p>Your account role is not allowed to access this page.</p>
        <p className="auth-link-text">
          Go to <Link to="/login">Login</Link>
        </p>
      </section>
    </main>
  );
}
