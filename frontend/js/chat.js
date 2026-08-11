// ============================================================
// CHAT PAGE
// ============================================================

// Resolve which conversation to open from ?userId=
const params = new URLSearchParams(
    window.location.search
);

const otherUserId = Number(
    params.get("userId")
);

if (!otherUserId) {
    window.location.href = "chats.html";
}

let activeConversationUser = null;

// STOMP client
let stompClient = null;


// ============================================================
// INITIALIZE CONVERSATION
// ============================================================

async function initConversation() {

    try {

        await loadSidebarChatList(otherUserId);

        activeConversationUser =
            sidebarConnectedUsers.find(
                (u) =>
                    Number(u.id) === otherUserId
            );


        if (!activeConversationUser) {

            toast(
                "That connection couldn't be found.",
                "error"
            );

            setTimeout(() => {

                window.location.href =
                    "chats.html";

            }, 800);

            return;
        }


        // --------------------------------------------------------
        // Chat header
        // --------------------------------------------------------

        renderAvatar(
            document.getElementById(
                "chatHeaderAvatar"
            ),
            activeConversationUser.fullName,
            activeConversationUser.profilePictureUrl
        );


        document.getElementById(
            "chatHeaderName"
        ).textContent =
            activeConversationUser.fullName ||
            "Unknown";


        document.getElementById(
            "chatHeaderSub"
        ).textContent =
            activeConversationUser.email ||
            activeConversationUser.phone ||
            "";


        // --------------------------------------------------------
        // Load previous messages
        // --------------------------------------------------------

        await loadMessages();


        // --------------------------------------------------------
        // Connect WebSocket
        // --------------------------------------------------------

        connectRealtime();


        document
            .getElementById("messageInput")
            .focus();

    }
    catch (error) {

        console.error(
            "Failed to initialize conversation:",
            error
        );

        toast(
            error.message ||
            "Failed to open chat.",
            "error"
        );
    }
}


// ============================================================
// LOAD PREVIOUS MESSAGES FROM DATABASE
// ============================================================

async function loadMessages() {

    const container =
        document.getElementById(
            "chatMessages"
        );

    container.innerHTML = "";


    try {

        const messages =
            await Api.getMessages(
                currentUser.userId,
                activeConversationUser.id
            );


        if (
            !Array.isArray(messages) ||
            messages.length === 0
        ) {

            container.innerHTML = `
                <div class="msg-day-divider">
                    Say hello 👋
                </div>
            `;

            return;
        }


        messages.forEach(
            (message) => {

                appendBubble(
                    normalizeMessage(message)
                );

            }
        );


        container.scrollTop =
            container.scrollHeight;

    }
    catch (error) {

        console.error(
            "Failed to load chat history:",
            error
        );

        container.innerHTML = `
            <div class="msg-day-divider">
                Unable to load previous messages.
            </div>
        `;
    }
}


// ============================================================
// CONVERT BACKEND MESSAGE TO FRONTEND FORMAT
// ============================================================
//
// Backend:
//
// {
//     id,
//     senderId,
//     receiverId,
//     message,
//     timestamp
// }
//
// Frontend:
//
// {
//     id,
//     senderId,
//     receiverId,
//     content,
//     sentAt
// }
//
// ============================================================

function normalizeMessage(message) {

    return {

        id: message.id,

        senderId:
            Number(message.senderId),

        receiverId:
            Number(message.receiverId),

        content:
            message.message || "",

        sentAt:
            message.timestamp
    };
}


// ============================================================
// DISPLAY MESSAGE
// ============================================================

function appendBubble(message) {

    const container =
        document.getElementById(
            "chatMessages"
        );


    const mine =
        Number(message.senderId) ===
        Number(currentUser.userId);


    const row =
        document.createElement("div");

    row.className =
        `bubble-row ${
            mine ? "mine" : "theirs"
        }`;


    const bubble =
        document.createElement("div");

    bubble.className =
        "bubble";


    const time =
        message.sentAt
            ? new Date(
                message.sentAt
            ).toLocaleTimeString(
                [],
                {
                    hour: "2-digit",
                    minute: "2-digit"
                }
            )
            : "";


    bubble.innerHTML = `
        ${escapeHtml(message.content)}
        <span class="bubble-time">
            ${time}
        </span>
    `;


    row.appendChild(bubble);

    container.appendChild(row);


    container.scrollTop =
        container.scrollHeight;
}


// ============================================================
// SEND MESSAGE
// ============================================================

const messageInput =
    document.getElementById(
        "messageInput"
    );


const sendMessageBtn =
    document.getElementById(
        "sendMessageBtn"
    );


async function handleSend() {

    const content =
        messageInput.value.trim();


    if (
        !content ||
        !activeConversationUser
    ) {
        return;
    }


    // Check WebSocket connection
    if (
        !stompClient ||
        !stompClient.connected
    ) {

        toast(
            "Chat connection is not available.",
            "error"
        );

        return;
    }


    // --------------------------------------------------------
    // Message object expected by backend
    // --------------------------------------------------------

    const message = {

        senderId:
            Number(currentUser.userId),

        receiverId:
            Number(activeConversationUser.id),

        message:
            content
    };


    console.log(
        "Sending message:",
        message
    );


    // --------------------------------------------------------
    // Send through STOMP
    // --------------------------------------------------------

    stompClient.publish({

        destination: "/app/chat",

        body:
            JSON.stringify(message)

    });


    // --------------------------------------------------------
    // Display our message immediately
    // --------------------------------------------------------
    //
    // Current backend sends the saved message to the receiver.
    // Therefore we display our own message immediately.
    //
    // The actual database timestamp will be available when
    // chat history is loaded again.
    // --------------------------------------------------------

    appendBubble({

        senderId:
            message.senderId,

        receiverId:
            message.receiverId,

        content:
            message.message,

        sentAt:
            new Date().toISOString()
    });


    // Clear input
    messageInput.value = "";
}


// ============================================================
// SEND BUTTON
// ============================================================

if (sendMessageBtn) {

    sendMessageBtn.addEventListener(
        "click",
        handleSend
    );
}


// ============================================================
// ENTER TO SEND
// ============================================================

if (messageInput) {

    messageInput.addEventListener(
        "keydown",
        function (event) {

            if (
                event.key === "Enter" &&
                !event.shiftKey
            ) {

                event.preventDefault();

                handleSend();
            }

        }
    );
}


// ============================================================
// CONNECT TO SPRING BOOT WEBSOCKET
// ============================================================

function connectRealtime() {

    // Close old connection if one exists
    if (stompClient) {

        stompClient.deactivate();

        stompClient = null;
    }


    setWsStatus(false);


    // --------------------------------------------------------
    // Create STOMP client
    // --------------------------------------------------------

    stompClient =
        new StompJs.Client({

            // Spring Boot:
            // registry.addEndpoint("/ws").withSockJS()
            webSocketFactory: function () {

                return new SockJS(
                    `${CONFIG.API_BASE_URL}/ws`
                );
            },


            // Automatically reconnect
            reconnectDelay: 5000,


            // ------------------------------------------------
            // Connected
            // ------------------------------------------------

            onConnect: function () {

                console.log(
                    "WebSocket connected."
                );


                setWsStatus(true);


                const userId =
                    Number(
                        currentUser.userId
                    );


                const destination =
                    `/topic/user/${userId}`;




                // ------------------------------------------------
                // Subscribe to current user's messages
                // ------------------------------------------------

                stompClient.subscribe(
                    destination,
                    function (frame) {



                        try {

                            const backendMessage =
                                JSON.parse(
                                    frame.body
                                );


                            const message =
                                normalizeMessage(
                                    backendMessage
                                );


                            // ------------------------------------------------
                            // Only show message if it belongs to the
                            // currently opened conversation
                            // ------------------------------------------------

                            const relevant =
                                (
                                    Number(
                                        message.senderId
                                    ) ===
                                    Number(
                                        activeConversationUser.id
                                    )
                                    &&
                                    Number(
                                        message.receiverId
                                    ) ===
                                    Number(
                                        currentUser.userId
                                    )
                                );


                            if (!relevant) {

                                return;
                            }


                            // Display received message
                            appendBubble(
                                message
                            );

                        }
                        catch (error) {

                            console.error(
                                "Invalid WebSocket message:",
                                error
                            );
                        }

                    }
                );

            },


            // ------------------------------------------------
            // STOMP ERROR
            // ------------------------------------------------

            onStompError: function (frame) {

                console.error(
                    "STOMP error:",
                    frame
                );

                setWsStatus(false);
            },


            // ------------------------------------------------
            // WebSocket ERROR
            // ------------------------------------------------

            onWebSocketError: function (error) {

                console.error(
                    "WebSocket error:",
                    error
                );

                setWsStatus(false);
            },


            // ------------------------------------------------
            // WebSocket CLOSED
            // ------------------------------------------------

            onWebSocketClose: function () {

                console.log(
                    "WebSocket connection closed."
                );

                setWsStatus(false);
            }

        });


    // Start WebSocket connection
    stompClient.activate();
}


// ============================================================
// WEBSOCKET STATUS
// ============================================================

function setWsStatus(isLive) {

    const badge =
        document.getElementById(
            "wsStatus"
        );


    if (!badge) {
        return;
    }


    badge.textContent =
        isLive
            ? "Live"
            : "Offline";


    badge.className =
        `conn-status ${
            isLive
                ? "live"
                : "offline"
        }`;
}


// ============================================================
// CLOSE WEBSOCKET WHEN LEAVING PAGE
// ============================================================

window.addEventListener(
    "beforeunload",
    function () {

        if (stompClient) {

            stompClient.deactivate();
        }

    }
);


// ============================================================
// START CHAT
// ============================================================

initConversation();