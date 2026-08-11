// ============================================================
// Auth guard — include this on every protected page, before any
// page-specific script that reads `currentUser`.
// ============================================================
const currentUser = JSON.parse(localStorage.getItem(CONFIG.STORAGE_KEY_USER) || "null");
if (!currentUser) {
  window.location.href = "index.html";
}

// ============================================================
// Toasts
// ============================================================
function toast(message, type = "") {
  const container = document.getElementById("toastContainer");
  if (!container) return;
  const el = document.createElement("div");
  el.className = `toast ${type}`;
  el.textContent = message;
  container.appendChild(el);
  setTimeout(() => el.remove(), 3200);
}

// ============================================================
// Avatars
// ============================================================
function initials(name) {
  if (!name) return "?";
  return name
    .trim()
    .split(/\s+/)
    .slice(0, 2)
    .map((p) => p[0]?.toUpperCase())
    .join("");
}

function renderAvatar(el, name, pictureUrl) {
  if (pictureUrl) {
    el.innerHTML = `<img src="${pictureUrl}" alt="${name || ""}" style="width:100%;height:100%;object-fit:cover;" />`;
  } else {
    el.textContent = initials(name);
  }
}

function escapeHtml(str) {
  const div = document.createElement("div");
  div.textContent = str;
  return div.innerHTML;
}

// ============================================================
// Logout — wire up on any page with a #logoutBtn
// ============================================================
document.addEventListener("DOMContentLoaded", () => {
  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
      localStorage.removeItem(CONFIG.STORAGE_KEY_USER);
      if (window.chatSocket) window.chatSocket.close();
      window.location.href = "index.html";
    });
  }

  updatePendingBadge();
});

// ============================================================
// Pending-requests nav badge — shown on every page's sidebar
// ============================================================
async function updatePendingBadge() {
  const badge = document.getElementById("pendingBadge");
  if (!badge) return;
  try {
    const pending = await Api.getPendingRequests(currentUser.userId);
    const count = Array.isArray(pending) ? pending.length : 0;
    if (count > 0) {
      badge.textContent = count;
      badge.classList.remove("hidden");
    } else {
      badge.classList.add("hidden");
    }
  } catch (err) {
    // Non-critical — fail silently, badge just stays hidden.
  }
}

// ============================================================
// Local message store — stand-in until MessageController exists.
// Shared by chats.html (previews) and chat.html (full conversation).
// See the README section "Chat / real-time messaging" for how to
// replace these with real API calls once the backend is built.
// ============================================================
function conversationKey(userId1, userId2) {
  return [userId1, userId2].sort((a, b) => a - b).join("_");
}

function getLocalMessages(key) {
  return JSON.parse(localStorage.getItem(CONFIG.STORAGE_KEY_MESSAGES_PREFIX + key) || "[]");
}

function saveLocalMessages(key, messages) {
  localStorage.setItem(CONFIG.STORAGE_KEY_MESSAGES_PREFIX + key, JSON.stringify(messages));
}

function getLastMessage(otherUserId) {
  const key = conversationKey(currentUser.userId, otherUserId);
  const messages = getLocalMessages(key);
  return messages.length ? messages[messages.length - 1] : null;
}

function formatRelativeTime(isoString) {
  const date = new Date(isoString);
  const now = new Date();
  const sameDay = date.toDateString() === now.toDateString();
  if (sameDay) return date.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });

  const yesterday = new Date(now);
  yesterday.setDate(now.getDate() - 1);
  if (date.toDateString() === yesterday.toDateString()) return "Yesterday";

  return date.toLocaleDateString([], { month: "short", day: "numeric" });
}

// ============================================================
// Sidebar chat list — used on chat.html to switch conversations
// while one is already open (compact, avatar + name only).
// ============================================================
let sidebarConnectedUsers = [];

async function loadSidebarChatList(activeUserId) {
  const list = document.getElementById("sidebarChatList");
  if (!list) return;

  try {
    const users = await Api.getConnectedUsers(currentUser.userId);
    sidebarConnectedUsers = Array.isArray(users) ? users : [];

    if (sidebarConnectedUsers.length === 0) {
      list.innerHTML = `<div class="list-empty">No connections yet.<br />Visit "Find people" to start a conversation.</div>`;
      return;
    }
    renderSidebarChatList(sidebarConnectedUsers, activeUserId);
  } catch (err) {
    list.innerHTML = `<div class="list-empty">Couldn't load chats: ${err.message}</div>`;
  }

  const filterInput = document.getElementById("chatFilterInput");
  if (filterInput) {
    filterInput.addEventListener("input", (e) => {
      const q = e.target.value.trim().toLowerCase();
      const filtered = sidebarConnectedUsers.filter(
        (u) => (u.fullName || "").toLowerCase().includes(q) || (u.email || "").toLowerCase().includes(q)
      );
      if (filtered.length === 0) {
        list.innerHTML = `<div class="list-empty">No matches.</div>`;
      } else {
        renderSidebarChatList(filtered, activeUserId);
      }
    });
  }
}

function renderSidebarChatList(users, activeUserId) {
  const list = document.getElementById("sidebarChatList");
  list.innerHTML = "";
  users.forEach((u) => {
    const row = document.createElement("a");
    row.className = "person-row";
    row.href = `chat.html?userId=${u.id}`;
    if (Number(activeUserId) === Number(u.id)) row.classList.add("active");
    row.style.textDecoration = "none";

    const avatar = document.createElement("div");
    avatar.className = "avatar";
    renderAvatar(avatar, u.fullName, u.profilePictureUrl);

    const info = document.createElement("div");
    info.className = "person-info";
    info.innerHTML = `<div class="person-name">${u.fullName || "Unknown"}</div><div class="person-sub">${u.email || u.phone || ""}</div>`;

    row.appendChild(avatar);
    row.appendChild(info);
    list.appendChild(row);
  });
}