# Bus Booking Application – Design Document

# 1. Overview

The Bus Booking Application is a full-stack web application that allows users to search buses, check seat availability, book tickets, cancel bookings, and manage travel history online. The system also provides an admin dashboard for managing buses, routes, bookings, users, and pricing information.

The backend is developed using Spring Boot (Java), while the frontend is built using React+Vite with responsive UI design.

---

# 2. Architecture

## 2.1 Backend

- Framework: Spring Boot (Java 17)
- Database: MySQL
- Authentication: JWT-based Authentication
- Persistence: JPA/Hibernate
- API: RESTful APIs
- Build Tool: Maven

---

## 2.2 Frontend

- Framework: React.js+Vite
- Styling: CSS / Tailwind CSS
- Routing: React Router
- API Communication: Axios
- State Management: React Hooks & Context API

---

# 3. Core Features

## 3.1 User Features

- User registration and login
- Search buses by source, destination, and travel date
- View bus details and seat availability
- Select seats and book tickets
- Cancel bookings
- View booking history
- Make payments securely

---

## 3.2 Admin Features

- Manage buses (Add/Edit/Delete)
- Manage routes
- Manage users
- Manage bookings
- Update ticket pricing
- Monitor system activities

---

# 4. Backend Design

## 4.1 Entities

### User
Stores user and admin details.

Fields:
- id
- name
- email
- password
- phone
- role

---

### Bus
Stores bus information.

Fields:
- id
- busName
- busNumber
- busType
- total seats

---

### Routes
Stores route details.

Fields:
- id
- busId
- fare
- source
- destination
- travel date
- arrival time
- departure time

---

### Seats
Stores seat details for buses.

Fields:
- id
- busId
- seatNumber
- seatType
- status

---

### Bookings
Stores booking information.

Fields:
- id
- userId
- routeId
- bookingDate
- totalFare
- bookingStatus

---

### BookingSeats
Stores mapping between booking and selected seats.

Fields:
- id
- bookingId
- seatId

---

### Payments
Stores payment transaction details.

Fields:
- id
- bookingId
- amount
- paymentMethod
- paymentStatus
- paymentDate

---

## 4.2 Security

### JWT Authentication
All protected APIs require a valid JWT token in the Authorization header.

### Role-Based Authorization
Access is restricted based on USER and ADMIN roles.

### Password Encryption
Passwords are encrypted using BCrypt before storing in the database.

---

## 4.3 API Endpoints

### Authentication APIs
- POST /api/auth/register
- POST /api/auth/login

---

### Bus APIs
- GET /api/buses
- GET /api/buses/{id}

---

### Booking APIs
- POST /api/bookings
- DELETE /api/bookings/{id}
- GET /api/bookings/user/{id}

---

### Payment APIs
- POST /api/payments
- GET /api/payments/status/{id}

---

### Admin APIs
- POST /api/admin/buses
- PUT /api/admin/buses/{id}
- DELETE /api/admin/buses/{id}
- GET /api/admin/bookings

---

## 4.4 Repositories

JPA repositories are used for database interaction.

Repositories:
- UserRepository
- BusRepository
- RouteRepository
- SeatRepository
- BookingRepository
- PaymentRepository

Custom queries are used for:
- Seat availability checking
- Booking history
- Search filtering

---

# 5. Frontend Design

## 5.1 State Management

### AuthContext
Manages:
- Authentication state
- JWT token
- User role

### Component State
Local state is used for:
- Forms
- Seat selection
- Booking details
- Search filters
- API responses

---

## 5.2 Pages & Components

### Authentication Pages
- Login Page
- Register Page

### User Pages
- Home Page
- Search Results Page
- Bus Details Page
- Seat Selection Page
- Booking Confirmation Page

### Admin Pages
- Admin Dashboard
- Manage Bus Page
- Manage Booking Page
- Manage User Page

---

## 5.3 API Integration

### Axios
Centralized Axios client is used for API communication with automatic JWT token injection.

### Error Handling
Frontend displays:
- Validation errors
- Booking failure messages
- Payment errors
- Loading indicators

---

## 5.4 UI/UX

### Responsive Design
Responsive layouts are implemented for desktop devices.

### Seat Selection UI
Interactive seat layout for selecting available seats.

### User Experience
Simple navigation and booking workflow.

---

# 6. Security Considerations

- JWT tokens stored securely
- Passwords never exposed in API responses
- Admin APIs protected using role-based authorization
- Input validation implemented on frontend and backend
- Secure API communication using HTTPS

---

# 7. Extensibility

The system is designed to support future enhancements such as:

- Live bus tracking
- Email notifications
- Dynamic pricing
- Mobile application support
- AI-based route recommendations

Modular architecture allows easy feature expansion.

---

# 8. Deployment

## Backend
- Spring Boot executable JAR
- Hosted on cloud server/local server

## Frontend
- React production build
- Deployable on Netlify/Vercel

## Database
- MySQL server configuration

---

# 9. Known Limitations

- No live GPS tracking
- No real payment gateway integration
- No SMS/email OTP verification
- No mobile application support
- Limited analytics in MVP version

---

# 10. Future Improvements

- Real-time bus tracking
- Email and SMS notifications
- QR code-based ticket verification
- Multi-language support
- Dynamic fare calculation
- Mobile app integration
- Online payment gateway integration

---

# 11. Layered Architecture Design

The project follows layered architecture:

## Controller Layer
Handles API requests and responses.

Controllers:
- AuthController
- BusController
- RouteController
- SeatController
- BookingController
- PaymentController

---

## Service Layer
Contains business logic.

Services:
- AuthService
- BusService
- RouteService
- SeatService
- BookingService
- PaymentService

---

## Repository Layer
Handles database operations using JPA repositories.

---

## Database Layer
Stores application data in MySQL database.

---

# 12. Conclusion

The Bus Booking Application provides a secure and scalable online reservation system for managing bus ticket bookings. The project demonstrates full-stack application development using React+Vite, Spring Boot, JWT authentication, REST APIs, and MySQL database integration.

The system ensures maintainability, scalability, security, and efficient management of bookings for both users and administrators.