// ============================================================
// Pending requests (people waiting for the current user to accept)
// ============================================================
async function loadPendingRequests() {
  const list = document.getElementById("pendingRequestsList");
  try {
    const pending = await Api.getPendingRequests(currentUser.userId);
    const requests = Array.isArray(pending) ? pending : [];

    if (requests.length === 0) {
      list.innerHTML = `<div class="empty-block">No pending connection requests right now.</div>`;
      return;
    }

    list.innerHTML = "";
    requests.forEach((req) => {
      // ASSUMPTION: ConnectionRequest entity fields — adjust if yours differ.
      const requestId = req.id ?? req.requestId;
      const senderId = req.senderId ?? req.sender?.id;
      const senderName = req.senderName ?? req.sender?.fullName ?? `User #${senderId}`;

      const row = document.createElement("div");
      row.className = "wide-row";

      const avatar = document.createElement("div");
      avatar.className = "avatar";
      renderAvatar(avatar, senderName, req.senderProfilePictureUrl);

      const info = document.createElement("div");
      info.className = "person-info";
      info.innerHTML = `<div class="person-name">${senderName}</div><div class="person-sub">wants to connect with you</div>`;

      const acceptBtn = document.createElement("button");
      acceptBtn.className = "btn btn-accent btn-sm";
      acceptBtn.textContent = "Accept";
      acceptBtn.addEventListener("click", async () => {
        acceptBtn.disabled = true;
        acceptBtn.textContent = "…";
        try {
          await Api.acceptConnection({ requestId, senderId, receiverId: currentUser.userId });
          toast(`You're now connected with ${senderName}.`, "success");
          loadPendingRequests();
          loadConnectedUsers();
          updatePendingBadge();
        } catch (err) {
          toast(err.message, "error");
          acceptBtn.disabled = false;
          acceptBtn.textContent = "Accept";
        }
      });

      row.appendChild(avatar);
      row.appendChild(info);
      row.appendChild(acceptBtn);
      list.appendChild(row);
    });
  } catch (err) {
    list.innerHTML = `<div class="empty-block">Couldn't load requests: ${err.message}</div>`;
  }
}

// ============================================================
// Connected people (cards) — the people this user is already
// connected with, each with a shortcut straight into the chat.
// ============================================================
async function loadConnectedUsers() {
  const list = document.getElementById("connectedUsersList");
  try {
    const users = await Api.getConnectedUsers(currentUser.userId);
    const connected = Array.isArray(users) ? users : [];

    if (connected.length === 0) {
      list.innerHTML = `<div class="empty-block">You're not connected with anyone yet. Try "Find people" to send a request.</div>`;
      return;
    }

    list.innerHTML = "";
    connected.forEach((user) => {
      const card = document.createElement("div");
      card.className = "wide-row";

      const avatar = document.createElement("div");
      avatar.className = "avatar";
      renderAvatar(avatar, user.fullName, user.profilePictureUrl);

      const info = document.createElement("div");
      info.className = "person-info";
      info.innerHTML = `<div class="person-name">${user.fullName || "Unknown"}</div><div class="person-sub">${user.email || user.phone || ""}</div>`;

      const messageBtn = document.createElement("a");
      messageBtn.className = "btn btn-ghost btn-sm";
      messageBtn.textContent = "Message";
      messageBtn.href = `chat.html?userId=${user.id}`;

      card.appendChild(avatar);
      card.appendChild(info);
      card.appendChild(messageBtn);
      list.appendChild(card);
    });
  } catch (err) {
    list.innerHTML = `<div class="empty-block">Couldn't load connections: ${err.message}</div>`;
  }
}

loadPendingRequests();
loadConnectedUsers();