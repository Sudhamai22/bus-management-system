import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function normalizeRole(role) {
  const cleanedRole = String(role || "").toUpperCase();
  return cleanedRole === "ADMIN" ? "ADMIN" : "USER";
}

export default function ProtectedRoute({ allowedRoles }) {
  const { isAuthenticated, user } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (
    Array.isArray(allowedRoles) &&
    allowedRoles.length > 0 &&
    !allowedRoles.map(normalizeRole).includes(normalizeRole(user?.role))
  ) {
    return <Navigate to="/unauthorized" replace />;
  }

  return <Outlet />;
}
