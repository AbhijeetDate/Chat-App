# Harbor — frontend for Project1

Plain HTML/CSS/JS, no build step, no framework. Open `index.html` in a
browser (or serve the folder with any static server) to run it.

## Pages

```
index.html               Log in                    (entry point)
signup.html               Sign up
forgot-password.html      Reset password (3-step OTP flow)

chats.html                 Chat list / dashboard (home after login)
chat.html?userId=123       One conversation (sidebar list + active chat)
connections.html           Pending connection requests
search.html                 Find people by email/phone, send requests
profile.html                Profile picture + change password
```

Every authenticated page shares the same sidebar navigation
(Chats · Connections · Find people · Profile · Log out) so moving
between them feels like one app, even though it's plain multi-page HTML.

## Supporting files

```
css/style.css        All styling (shared by every page)
js/config.js          ⭐ ONLY file to edit after you deploy the backend
js/api.js             Every fetch() call, one function per endpoint
js/shared.js          Auth guard, sidebar chat list, badge, logout, toasts, avatars — loaded on every protected page
js/login.js            index.html
js/signup.js            signup.html
js/forgot-password.js   forgot-password.html
js/chat.js             chat.html (chat + WebSocket stub)
js/connections.js       connections.html
js/search.js            search.html
js/profile.js           profile.html
```

## Deploying: the one variable to change

Open `js/config.js`:

```js
const API_BASE_URL = "http://localhost:8080";
```

Change this single line to your deployed backend URL (e.g.
`https://your-app.onrender.com`). The WebSocket URL used later for
real-time chat is derived from it automatically. No other file needs
touching.

## Endpoints wired up (from the controllers you shared)

| Feature | Method | Path |
|---|---|---|
| Sign up | POST | `/api/signup` |
| Log in | POST | `/api/login` |
| Change password | PUT | `/api/change-password` |
| Send OTP | POST | `/api/send-otp` |
| Verify OTP | POST | `/api/verify-otp` |
| Reset password | PUT | `/api/reset-password` |
| Search user | POST | `/api/search-user` |
| Update profile picture | PUT | `/api/profile/picture` (multipart: `userId`, `image`) |
| Remove profile picture | PUT | `/api/profile/remove-picture` |
| Send connection request | POST | `/api/connections/send-connection-request` |
| Get pending requests | GET | `/api/connections/pending/{receiverId}` |
| Accept connection | PUT | `/api/connections/accept` |
| Get connected users | GET | `/api/connections/get-connected-users/{userId}` |

## ⚠️ Assumptions to double check

Three DTOs weren't in the code you shared, so I guessed reasonable field
names. Each spot is marked `ASSUMPTION` in `js/api.js` / `js/connections.js` —
search for that word to find all of them:

1. **`RemoveProfilePictureRequest`** — assumed `{ userId }`.
2. **`SendConnectionRequest`** — assumed `{ senderId, receiverId }`.
3. **`AcceptConnectionRequest`** — assumed it might read `requestId`,
   `senderId`, and/or `receiverId`, so the frontend sends all three
   to be safe. Trim this once you see the real DTO.
4. **`ConnectionRequest` entity / `ConnectedUsersResponse` fields** — assumed
   shapes similar to `SearchUserResponse` (`id`, `fullName`, `email`, `phone`,
   `profilePictureUrl`), with a few fallback field names in `connections.js`
   in case yours differ slightly.

If any of these are wrong, you only need to edit the one function in
`api.js` (or the small rendering block in `connections.js`) that builds
that request/reads that response — nothing else depends on the exact shape.

## Chat / real-time messaging (backend not built yet)

Since there's no `MessageController` yet, chat currently works as a
**local-only stand-in** on `chat.html`:

- Messages are stored per-conversation in `localStorage`
  (`chatapp_messages_<sorted-user-ids>`), so the UI is fully functional and
  testable today — it just doesn't sync between two different browsers/users.
- `js/chat.js` has two clearly marked `TODO(backend)` blocks showing exactly
  what to swap in once the backend exists:
  1. `loadMessages()` — replace the localStorage read with
     `Api.getMessages(currentUser.id, activeConversationUser.id)`.
  2. `handleSend()` — send the message over the WebSocket and/or via
     `Api.sendMessage(message)`.
- `connectRealtime()` is already called every time the page loads, and is a
  documented no-op — a full plain-WebSocket implementation is written out in
  a comment block right above it. If your backend uses Spring's
  STOMP-over-SockJS instead of a plain WebSocket (the common pattern), swap
  in `sockjs-client` + `@stomp/stompjs` there instead — the comment explains
  both options.
- `Api.getMessages` / `Api.sendMessage` in `js/api.js` are already defined
  with a conventional REST shape (`GET /api/messages/{id1}/{id2}`,
  `POST /api/messages/send`) — adjust the paths/fields to match whatever you
  build.

Once you build the real backend, the UI itself won't need to change —
just the two TODO blocks and the WebSocket stub in `chat.js`.

## Notes

- Auth state lives in `localStorage` under `chatapp_user` (no JWT/session
  handling in the backend yet, so this is a simple "remember who's logged
  in" flag, not a security boundary).
- Every protected page starts with `js/shared.js`, which redirects to
  `index.html` if no user is stored — so you can't reach `chats.html`,
  `chat.html`, `connections.html`, `search.html`, or `profile.html`
  without logging in first.
- Fully responsive: single column with a back button on narrow/mobile
  screens, two-column layout on desktop.
