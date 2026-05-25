import api from "./authApi";

export const getBuses = () => api.get("/buses").then((response) => response.data);
export const createBus = (payload) => api.post("/buses", payload).then((response) => response.data);
export const updateBus = (id, payload) => api.put(`/buses/${id}`, payload).then((response) => response.data);
export const deleteBus = (id) => api.delete(`/buses/${id}`);

export const getRoutes = () => api.get("/routes").then((response) => response.data);
export const createRoute = (payload) => api.post("/routes", payload).then((response) => response.data);
export const updateRoute = (id, payload) => api.put(`/routes/${id}`, payload).then((response) => response.data);
export const deleteRoute = (id) => api.delete(`/routes/${id}`);

export const getBookings = () => api.get("/bookings").then((response) => response.data);
export const cancelBooking = (id) => api.delete(`/bookings/cancel/${id}`);

export const getUsers = () => api.get("/users").then((response) => response.data);
export const updateUser = (id, payload) => api.put(`/users/${id}`, payload).then((response) => response.data);
export const blockUser = (id) => api.put(`/users/${id}/block`).then((response) => response.data);
export const unblockUser = (id) => api.put(`/users/${id}/unblock`).then((response) => response.data);
export const deleteUser = (id) => api.delete(`/users/${id}`);

export const getAdminReports = () => api.get("/admin/reports").then((response) => response.data);