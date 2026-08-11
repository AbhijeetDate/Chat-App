let allChatUsers = [];

async function loadChatCards() {
  const list = document.getElementById("chatCardsList");
  try {
    const users = await Api.getConnectedUsers(currentUser.userId);
    allChatUsers = Array.isArray(users) ? users : [];

    if (allChatUsers.length === 0) {
      list.innerHTML = `<div class="empty-block">No connections yet. Go to "Find people" to connect with someone and start chatting.</div>`;
      return;
    }
    renderChatCards(allChatUsers);
  } catch (err) {
    list.innerHTML = `<div class="empty-block">Couldn't load chats: ${err.message}</div>`;
  }
}

function renderChatCards(users) {
  const list = document.getElementById("chatCardsList");

  // Pair each connection with its last local message (if any), so
  // people you've actually messaged float to the top, newest first,
  // and everyone else (an "empty" chat you haven't started yet)
  // follows, sorted alphabetically.
  //
  // TODO(backend): once GET /api/messages/{id1}/{id2} exists, this
  // preview + sort should be driven by real message history instead
  // of the localStorage stand-in — see getLastMessage() in shared.js.
  const withPreview = users.map((user) => ({ user, last: getLastMessage(user.id) }));

  withPreview.sort((a, b) => {
    if (a.last && b.last) return new Date(b.last.sentAt) - new Date(a.last.sentAt);
    if (a.last) return -1;
    if (b.last) return 1;
    return (a.user.fullName || "").localeCompare(b.user.fullName || "");
  });

  if (withPreview.length === 0) {
    list.innerHTML = `<div class="empty-block">No matches.</div>`;
    return;
  }

  list.innerHTML = "";
  withPreview.forEach(({ user, last }) => {
    const card = document.createElement("a");
    card.className = "wide-row";
    card.href = `chat.html?userId=${user.id}`;

    const avatar = document.createElement("div");
    avatar.className = "avatar";
    renderAvatar(avatar, user.fullName, user.profilePictureUrl);

    const info = document.createElement("div");
    info.className = "person-info";
    const previewText = last
      ? escapeHtml(last.content)
      : `<span style="font-style:italic;">No messages yet — say hello 👋</span>`;
    info.innerHTML = `<div class="person-name">${user.fullName || "Unknown"}</div><div class="person-sub">${previewText}</div>`;

    card.appendChild(avatar);
    card.appendChild(info);

    if (last) {
      const meta = document.createElement("div");
      meta.className = "row-meta";
      meta.textContent = formatRelativeTime(last.sentAt);
      card.appendChild(meta);
    }

    list.appendChild(card);
  });
}

document.getElementById("chatFilterInput").addEventListener("input", (e) => {
  const q = e.target.value.trim().toLowerCase();
  const filtered = allChatUsers.filter((u) => (u.fullName || "").toLowerCase().includes(q) || (u.email || "").toLowerCase().includes(q));
  renderChatCards(filtered);
});

loadChatCards();
