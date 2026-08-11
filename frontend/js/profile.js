function paintProfile() {
  document.getElementById("profileName").textContent = currentUser.fullName || "—";
  document.getElementById("profileEmail").textContent = currentUser.email || "—";
  renderAvatar(document.getElementById("profileAvatar"), currentUser.fullName, currentUser.profilePictureUrl);
}
paintProfile();

document.getElementById("profilePicInput").addEventListener("change", async (e) => {
  const file = e.target.files[0];
  if (!file) return;
  try {
    const res = await Api.updateProfilePicture(currentUser.userId, file);
    currentUser.profilePictureUrl = res.imageUrl;
    localStorage.setItem(CONFIG.STORAGE_KEY_USER, JSON.stringify(currentUser));
    paintProfile();
    toast("Profile picture updated.", "success");
  } catch (err) {
    toast(err.message, "error");
  } finally {
    e.target.value = "";
  }
});

document.getElementById("removePicBtn").addEventListener("click", async () => {
  try {
    await Api.removeProfilePicture(currentUser.userId);
    currentUser.profilePictureUrl = null;
    localStorage.setItem(CONFIG.STORAGE_KEY_USER, JSON.stringify(currentUser));
    paintProfile();
    toast("Profile picture removed.", "success");
  } catch (err) {
    toast(err.message, "error");
  }
});

document.getElementById("changePasswordForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const btn = e.target.querySelector("button[type=submit]");
  btn.disabled = true;
  try {
    const message = await Api.changePassword({
      email: currentUser.email,
      oldPassword: document.getElementById("cpOld").value,
      newPassword: document.getElementById("cpNew").value,
      confirmPassword: document.getElementById("cpConfirm").value,
    });
    const ok = message === "Password changed successfully.";
    toast(message, ok ? "success" : "error");
    if (ok) e.target.reset();
  } catch (err) {
    toast(err.message, "error");
  } finally {
    btn.disabled = false;
  }
});
