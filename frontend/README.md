# SmartBus Frontend (JWT Auth + Role Access)

This frontend includes:
- Login page
- Register page
- JWT token authentication
- Role-based authorization for USER and ADMIN
- Protected user and admin dashboards

## Run

```bash
npm install
npm run dev
```

## Environment

Create `.env` from `.env.example`:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

## Expected auth APIs

- POST /api/auth/register
- POST /api/auth/login

Expected success response should include token and optional user object:

```json
{
  "token": "jwt_token_here",
  "user": {
    "name": "John",
    "email": "john@example.com",
    "role": "USER"
  }
}
```

If user object is missing, this app extracts role from JWT payload.
