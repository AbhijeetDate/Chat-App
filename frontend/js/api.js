/**
 * ============================================================
 *  API layer — every function here maps 1:1 to a backend endpoint.
 *  Nothing else in the app should call fetch() directly.
 * ============================================================
 */

async function apiRequest(path, { method = "GET", body, isForm = false } = {}) {
  const options = { method, headers: {} };

  if (isForm) {
    options.body = body; // FormData sets its own Content-Type boundary
  } else if (body !== undefined) {
    options.headers["Content-Type"] = "application/json";
    options.body = JSON.stringify(body);
  }

  let response;
  try {
    response = await fetch(`${CONFIG.API_BASE_URL}${path}`, options);
  } catch (err) {
    throw new Error(
      "Could not reach the server. Check that the backend is running and CONFIG.API_BASE_URL is correct."
    );
  }

  const contentType = response.headers.get("content-type") || "";
  const payload = contentType.includes("application/json")
    ? await response.json().catch(() => null)
    : await response.text();

  if (!response.ok) {
    const message =
      (payload && typeof payload === "object" && payload.message) ||
      (typeof payload === "string" && payload) ||
      `Request failed (${response.status})`;
    throw new Error(message);
  }

  return payload;
}

const Api = {
  // ---------- User (UserController) ----------
  signup(data) {
    // data: { fullName, email, phone, password }
    return apiRequest("/api/signup", { method: "POST", body: data });
  },

  login(data) {
    // data: { email, password }
    // returns LoginResponse: { message, id, fullName, email, profilePictureUrl }
    return apiRequest("/api/login", { method: "POST", body: data });
  },

  changePassword(data) {
    // data: { email, oldPassword, newPassword, confirmPassword }
    return apiRequest("/api/change-password", { method: "PUT", body: data });
  },

  sendOtp(data) {
    // data: { email, phone }
    return apiRequest("/api/send-otp", { method: "POST", body: data });
  },

  verifyOtp(data) {
    // data: { email, otp }
    return apiRequest("/api/verify-otp", { method: "POST", body: data });
  },

  resetPassword(data) {
    // data: { email, newPassword, confirmPassword }
    return apiRequest("/api/reset-password", { method: "PUT", body: data });
  },

  searchUser(data) {
    // data: { keyword }  (email or phone)
    // returns SearchUserResponse: { id, fullName, email, phone, profilePictureUrl }
    return apiRequest("/api/search-user", { method: "POST", body: data });
  },

  // ---------- Profile (ProfileController) ----------
  updateProfilePicture(userId, file) {
    const form = new FormData();
    form.append("userId", userId);
    form.append("image", file);
    return apiRequest("/api/profile/picture", { method: "PUT", body: form, isForm: true });
  },

  removeProfilePicture(userId) {
    // ASSUMPTION: RemoveProfilePictureRequest DTO isn't shown in the code you shared.
    // Guessed shape: { userId }. If your DTO uses different field names,
    // this is the ONLY place you need to change it.
    return apiRequest("/api/profile/remove-picture", { method: "PUT", body: { userId } });
  },

  // ---------- Connections (ConnectionRequestController) ----------
  sendConnectionRequest(senderId, receiverId) {
    // ASSUMPTION: SendConnectionRequest DTO guessed as { senderId, receiverId }.
    // Adjust field names here if your DTO differs.
    return apiRequest("/api/connections/send-connection-request", {
      method: "POST",
      body: { senderId, receiverId },
    });
  },

  getPendingRequests(receiverId) {
    // returns List<ConnectionRequest> — entity fields assumed to include
    // at least: id, senderId, receiverId, status (see connections.js normalizePending)
    return apiRequest(`/api/connections/pending/${receiverId}`, { method: "GET" });
  },

  acceptConnection(requestPayload) {
    // ASSUMPTION: AcceptConnectionRequest DTO guessed as { requestId, senderId, receiverId }.
    // We send all three so it works whichever field(s) your DTO actually reads;
    // trim this down once you see the real DTO.
    return apiRequest("/api/connections/accept", { method: "PUT", body: requestPayload });
  },

  getConnectedUsers(userId) {
    // returns List<ConnectedUsersResponse> — fields assumed similar to
    // SearchUserResponse: { id, fullName, email, phone, profilePictureUrl }
    return apiRequest(`/api/connections/get-connected-users/${userId}`, { method: "GET" });
  },

  // ---------- Messages (NOT built on backend yet) ----------
  // These are placeholders matching a conventional REST shape.
  // Swap the paths/fields once MessageController exists — see chat.js
  // for where local-storage stand-ins are used instead, and where to
  // switch them out.
  getMessages(userId1, userId2) {
    return apiRequest(`/api/messages/${userId1}/${userId2}`, { method: "GET" });
  },

  // sendMessage(data) {
  //   // data: { senderId, receiverId, content }
  //   return apiRequest("/api/messages/send", { method: "POST", body: data });
  // },
};
