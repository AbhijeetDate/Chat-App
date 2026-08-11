let searchDebounce;

document.getElementById("searchInput").addEventListener("input", (e) => {
    clearTimeout(searchDebounce);

    const keyword = e.target.value.trim();
    const list = document.getElementById("searchResultsList");

    if (!keyword) {
        list.innerHTML = '<div class="empty-block">Results will appear here once you start typing.</div>';
        return;
    }

    searchDebounce = setTimeout(async () => {
        list.innerHTML = '<div class="empty-block">Searching...</div>';

        try {
            const result = await Api.searchUser({
                keyword: keyword,
                currentUserId: currentUser.userId
            });

            console.log("Search result:", result);

            renderSearchResult(result);

        } catch (err) {
            console.error("Search error:", err);

            list.innerHTML =
                '<div class="empty-block">' +
                err.message +
                '</div>';
        }
    }, 400);
});

function renderSearchResult(user) {

    const list = document.getElementById("searchResultsList");

    if (!user) {
        list.innerHTML =
            '<div class="empty-block">' +
            'No user found with that email or phone number.' +
            '</div>';
        return;
    }

    if (!currentUser || currentUser.userId == null) {

        console.error(
            "Logged-in user ID is missing:",
            currentUser
        );

        list.innerHTML =
            '<div class="empty-block">' +
            'Unable to identify the logged-in user. Please log in again.' +
            '</div>';

        return;
    }

    if (Number(user.id) === Number(currentUser.userId)) {

        list.innerHTML =
            '<div class="empty-block">' +
            "That's you!" +
            '</div>';

        return;
    }

    list.innerHTML = "";

    const row = document.createElement("div");
    row.className = "wide-row";

    const avatar = document.createElement("div");
    avatar.className = "avatar";

    renderAvatar(
        avatar,
        user.fullName,
        user.profilePictureUrl
    );

    const info = document.createElement("div");
    info.className = "person-info";

    info.innerHTML =
        '<div class="person-name">' +
        user.fullName +
        '</div>' +
        '<div class="person-sub">' +
        (user.email || user.phone || "") +
        '</div>';

    const connectBtn = document.createElement("button");
    connectBtn.className = "btn btn-accent btn-sm";

    if (user.connectedStatus === true) {

        connectBtn.textContent = "Connected";
        connectBtn.disabled = true;

    } else {

        connectBtn.textContent = "Connect";

        connectBtn.addEventListener("click", async () => {

            connectBtn.disabled = true;
            connectBtn.textContent = "...";

            try {

                const senderId = currentUser.userId;
                const receiverId = user.id;

                console.log(
                    "Sending connection request:",
                    {
                        senderId: senderId,
                        receiverId: receiverId
                    }
                );

                if (senderId == null) {
                    throw new Error(
                        "Logged-in user ID is missing. Please log in again."
                    );
                }

                if (receiverId == null) {
                    throw new Error(
                        "Receiver user ID is missing."
                    );
                }

                const msg = await Api.sendConnectionRequest(
                    senderId,
                    receiverId
                );

                toast(
                    typeof msg === "string"
                        ? msg
                        : "Connection request sent.",
                    "success"
                );

                connectBtn.textContent = "Requested";

            } catch (err) {

                console.error(
                    "Connection request error:",
                    err
                );

                toast(
                    err.message,
                    "error"
                );

                connectBtn.disabled = false;
                connectBtn.textContent = "Connect";
            }
        });
    }

    row.appendChild(avatar);
    row.appendChild(info);
    row.appendChild(connectBtn);

    list.appendChild(row);
}