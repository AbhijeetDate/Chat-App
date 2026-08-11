if (localStorage.getItem(CONFIG.STORAGE_KEY_USER)) {
  window.location.href = "chats.html";
}

const statusMsg = document.getElementById("statusMsg");

function showStatus(message, type = "error") {
  statusMsg.textContent = message;
  statusMsg.className = `status-msg show ${type}`;
}

function clearStatus() {
  statusMsg.className = "status-msg";
}

let fpEmailCache = "";

function fpGoToStep(step) {
  document.querySelectorAll(".step-dot").forEach((dot) =>
    dot.classList.toggle("active", Number(dot.dataset.step) <= step)
  );
  document.getElementById("fpStep1").classList.toggle("hidden", step !== 1);
  document.getElementById("fpStep2").classList.toggle("hidden", step !== 2);
  document.getElementById("fpStep3").classList.toggle("hidden", step !== 3);
}

document.getElementById("fpSendOtpBtn").addEventListener("click", async (e) => {
  clearStatus();
  const btn = e.target;
  btn.disabled = true;
  try {
    const email = document.getElementById("fpEmail").value.trim();
    const phone = document.getElementById("fpPhone").value.trim();
    const message = await Api.sendOtp({ email, phone });

    if (message !== "OTP generated successfully.") {
      showStatus(message);
      return;
    }
    fpEmailCache = email;
    showStatus("OTP sent — check your email.", "success");
    fpGoToStep(2);
  } catch (err) {
    showStatus(err.message);
  } finally {
    btn.disabled = false;
  }
});

document.getElementById("fpVerifyOtpBtn").addEventListener("click", async (e) => {
  clearStatus();
  const btn = e.target;
  btn.disabled = true;
  try {
    const otp = document.getElementById("fpOtp").value.trim();
    const message = await Api.verifyOtp({ email: fpEmailCache, otp });

    if (message !== "OTP verified successfully.") {
      showStatus(message);
      return;
    }
    showStatus("OTP verified.", "success");
    fpGoToStep(3);
  } catch (err) {
    showStatus(err.message);
  } finally {
    btn.disabled = false;
  }
});

document.getElementById("fpResetBtn").addEventListener("click", async (e) => {
  clearStatus();
  const btn = e.target;
  btn.disabled = true;
  try {
    const newPassword = document.getElementById("fpNewPassword").value;
    const confirmPassword = document.getElementById("fpConfirmPassword").value;
    const message = await Api.resetPassword({ email: fpEmailCache, newPassword, confirmPassword });

    if (message !== "Password reset successfully.") {
      showStatus(message);
      return;
    }
    showStatus("Password reset — redirecting to log in…", "success");
    setTimeout(() => (window.location.href = "index.html"), 1000);
  } catch (err) {
    showStatus(err.message);
  } finally {
    btn.disabled = false;
  }
});
