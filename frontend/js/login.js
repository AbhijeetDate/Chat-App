// If already logged in, skip straight to the app
if (localStorage.getItem(CONFIG.STORAGE_KEY_USER)) {
window.location.href = "chats.html";
}

const statusMsg = document.getElementById("statusMsg");

function showStatus(message, type = "error") {
statusMsg.textContent = message;
statusMsg.className = `status-msg show ${type}`;
}

document.getElementById("loginForm").addEventListener("submit", async (e) => {
e.preventDefault();


const submitBtn = e.target.querySelector("button[type=submit]");
submitBtn.disabled = true;

try {
    const email = document.getElementById("loginEmail").value.trim();
    const password = document.getElementById("loginPassword").value;

    // Call login API
    const res = await Api.login({
        email,
        password
    });

    // Check login response
    if (!res || res.message !== "Login successful.") {
        showStatus(res?.message || "Invalid credentials.");
        return;
    }

    // Make sure the backend returned the user ID
    if (res.userId == null) {
        console.error("Login response does not contain userId:", res);
        showStatus("Login failed: user ID was not returned by the server.");
        return;
    }

    // Create the logged-in user object
    // IMPORTANT: Backend uses 'userId', not 'id'
    const user = {
        userId: res.userId,
        fullName: res.fullName,
        email: res.email,
        profilePictureUrl: res.profilePictureUrl
    };

    console.log("Login response:", res);
    console.log("Logged-in user:", user);

    // Store user information for the rest of the application
    localStorage.setItem(
        CONFIG.STORAGE_KEY_USER,
        JSON.stringify(user)
    );

    // Go to the main application
    window.location.href = "chats.html";

} catch (err) {
    console.error("Login error:", err);
    showStatus(err.message || "Unable to login. Please try again.");
} finally {
    submitBtn.disabled = false;
}
});
