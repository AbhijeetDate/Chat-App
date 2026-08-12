/**
 * ============================================================
 *  CONFIG — the ONLY thing you need to change after deployment
 * ============================================================
 * Local dev (Spring Boot default):   http://localhost:8080
 * After deployment, replace with your live backend URL, e.g.:
 *   const API_BASE_URL = "https://your-backend.onrender.com";
 *
 * Do NOT add a trailing slash.
 * ============================================================
 */
const API_BASE_URL = "https://chat-app-java-backend.onrender.com";

// WebSocket URL is derived automatically from API_BASE_URL
// (http -> ws, https -> wss). Used later for real-time chat.
const WS_BASE_URL = API_BASE_URL.replace(/^http/, "ws");

const CONFIG = {
  API_BASE_URL,
  WS_BASE_URL,
  STORAGE_KEY_USER: "chatapp_user",
  STORAGE_KEY_MESSAGES_PREFIX: "chatapp_messages_", // + conversationKey, local-only until real backend exists
};
