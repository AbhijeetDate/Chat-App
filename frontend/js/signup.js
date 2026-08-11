if (localStorage.getItem(CONFIG.STORAGE_KEY_USER)) {
  window.location.href = "chats.html";
}

const statusMsg = document.getElementById("statusMsg");

function showStatus(message, type = "error") {
  statusMsg.textContent = message;
  statusMsg.className = `status-msg show ${type}`;
}

document.getElementById("signupForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const submitBtn = e.target.querySelector("button[type=submit]");
  submitBtn.disabled = true;

  try {
    const payload = {
      fullName: document.getElementById("suName").value.trim(),
      email: document.getElementById("suEmail").value.trim(),
      phone: document.getElementById("suPhone").value.trim(),
      password: document.getElementById("suPassword").value,
    };

    const message = await Api.signup(payload);

    if (message !== "Signup successful.") {
      showStatus(message);
      return;
    }

    showStatus("Account created — redirecting to log in…", "success");
    setTimeout(() => (window.location.href = "index.html"), 900);
  } catch (err) {
    showStatus(err.message);
  } finally {
    submitBtn.disabled = false;
  }
});
