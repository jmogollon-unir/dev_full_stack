const state = {
  gatewayUrl: "http://localhost:8080",
  ws: null,
  books: []
};

const els = {
  apiDot: document.querySelector("#apiDot"),
  apiStatus: document.querySelector("#apiStatus"),
  wsDot: document.querySelector("#wsDot"),
  wsStatus: document.querySelector("#wsStatus"),
  gatewayUrl: document.querySelector("#gatewayUrl"),
  userId: document.querySelector("#userId"),
  messages: document.querySelector("#messages"),
  results: document.querySelector("#results"),
  chatForm: document.querySelector("#chatForm"),
  messageInput: document.querySelector("#messageInput"),
  settingsForm: document.querySelector("#settingsForm"),
  clearChat: document.querySelector("#clearChat"),
  refreshBooks: document.querySelector("#refreshBooks"),
  orderForm: document.querySelector("#orderForm"),
  bookId: document.querySelector("#bookId"),
  quantity: document.querySelector("#quantity")
};

function addMessage(role, text) {
  const message = document.createElement("div");
  message.className = `message ${role}`;
  message.textContent = text;
  els.messages.appendChild(message);
  els.messages.scrollTop = els.messages.scrollHeight;
}

function setApiStatus(ok, text) {
  els.apiDot.classList.toggle("ok", ok);
  els.apiStatus.textContent = text;
}

function setWsStatus(status, text) {
  els.wsDot.className = `dot ${status}`;
  els.wsStatus.textContent = text;
}

function endpoint(path) {
  return `${state.gatewayUrl}${path}`;
}

async function request(path, options = {}) {
  const response = await fetch(endpoint(path), {
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {})
    },
    ...options
  });

  if (!response.ok) {
    const body = await response.text();
    throw new Error(body || `HTTP ${response.status}`);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

function formatCurrency(value) {
  return new Intl.NumberFormat("es-ES", {
    style: "currency",
    currency: "EUR"
  }).format(Number(value || 0));
}

function renderBooks(books) {
  els.results.innerHTML = "";

  if (!books.length) {
    els.results.innerHTML = "<p class=\"hint\">No hay resultados para mostrar.</p>";
    return;
  }

  books.forEach((book) => {
    const item = document.createElement("article");
    item.className = "result-item";
    item.innerHTML = `
      <strong>${book.title || `Libro ${book.bookId}`}</strong>
      <div class="result-meta">${book.author || "Autor no disponible"} · ${book.genreName || "Sin género"}</div>
      <div class="result-meta">ID ${book.bookId} · Stock ${book.stock ?? "N/D"} · <span class="price">${formatCurrency(book.price)}</span></div>
    `;
    els.results.appendChild(item);
  });
}

function renderOrders(orders) {
  els.results.innerHTML = "";

  if (!orders.length) {
    els.results.innerHTML = "<p class=\"hint\">No se encontraron pedidos para ese usuario.</p>";
    return;
  }

  orders.forEach((order) => {
    const item = document.createElement("article");
    item.className = "result-item";
    item.innerHTML = `
      <strong>Pedido #${order.orderId}</strong>
      <div class="result-meta">Estado: ${order.status_name || order.statusName || "Sin estado"}</div>
      <div class="result-meta">Total: <span class="price">${formatCurrency(order.total)}</span></div>
      <div class="result-meta">${order.city || ""} ${order.country || ""}</div>
    `;
    els.results.appendChild(item);
  });
}

async function loadBooks(criteria = "") {
  const query = criteria ? `?${criteria}` : "";
  const books = await request(`/api/books${query}`);
  state.books = books;
  renderBooks(books.slice(0, 8));
  setApiStatus(true, "Gateway conectado");
  return books;
}

async function getOrder(orderId) {
  const order = await request(`/api/orders/${orderId}`);
  renderOrders([order]);
  return order;
}

async function getUserOrders(userId) {
  const orders = await request(`/api/orders/user/${userId}`);
  renderOrders(orders);
  return orders;
}

async function createOrder(bookId, quantity) {
  const order = await request("/api/orders", {
    method: "POST",
    body: JSON.stringify({
      userId: Number(els.userId.value || 1),
      items: [
        {
          bookId: Number(bookId),
          quantity: Number(quantity)
        }
      ]
    })
  });

  renderOrders([order]);
  return order;
}

function connectWebSocket() {
  if (state.ws) {
    state.ws.close();
  }

  const wsUrl = state.gatewayUrl.replace(/^http/, "ws") + "/ws-api/v1/communications/customer-agent";
  setWsStatus("muted", "Conectando WebSocket");

  try {
    state.ws = new WebSocket(wsUrl);
  } catch (error) {
    setWsStatus("muted", "WebSocket no disponible");
    return;
  }

  state.ws.onopen = () => setWsStatus("ok", "WebSocket conectado");
  state.ws.onclose = () => setWsStatus("muted", "WebSocket no disponible");
  state.ws.onerror = () => setWsStatus("muted", "WebSocket no disponible");
  state.ws.onmessage = (event) => addMessage("agent", event.data);
}

function sendToWebSocket(text) {
  if (state.ws && state.ws.readyState === WebSocket.OPEN) {
    state.ws.send(JSON.stringify({ message: text, userId: Number(els.userId.value || 1) }));
    return true;
  }
  return false;
}

async function answerLocally(text) {
  const normalized = text.toLowerCase().trim();

  if (sendToWebSocket(text)) {
    addMessage("agent", "He enviado tu consulta al canal WebSocket de communications.");
    return;
  }

  if (normalized.includes("ayuda")) {
    addMessage("agent", "Puedo buscar libros por título o autor, consultar 'pedido 3', ver 'pedidos del usuario 1' y crear una compra demo con 'comprar 1 x 2'.");
    return;
  }

  const orderMatch = normalized.match(/pedido\s*#?\s*(\d+)/);
  if (orderMatch && !normalized.includes("usuario")) {
    const order = await getOrder(orderMatch[1]);
    addMessage("agent", `Encontré el pedido #${order.orderId}. Total: ${formatCurrency(order.total)}. El correo de confirmación lo gestiona communications de forma asincrónica.`);
    return;
  }

  const userOrdersMatch = normalized.match(/usuario\s*#?\s*(\d+)/);
  if (normalized.includes("pedidos") && userOrdersMatch) {
    const orders = await getUserOrders(userOrdersMatch[1]);
    addMessage("agent", `Encontré ${orders.length} pedido(s) para el usuario ${userOrdersMatch[1]}.`);
    return;
  }

  const buyMatch = normalized.match(/comprar\s+(\d+)\s*x\s*(\d+)/);
  if (buyMatch) {
    const order = await createOrder(buyMatch[1], buyMatch[2]);
    addMessage("agent", `Pedido #${order.orderId} creado. Orders publicará el evento y communications enviará el correo de confirmación.`);
    return;
  }

  if (normalized.includes("popular")) {
    const books = await loadBooks("visible=true&popularity=5");
    addMessage("agent", `Te muestro ${Math.min(books.length, 8)} libro(s) populares disponibles en catálogo.`);
    return;
  }

  if (normalized.includes("buscar") || normalized.includes("libro")) {
    const query = normalized
      .replace("buscar", "")
      .replace("libros", "")
      .replace("libro", "")
      .trim();
    const params = query ? `title=${encodeURIComponent(query)}` : "";
    const books = await loadBooks(params);
    addMessage("agent", books.length ? `Encontré ${books.length} resultado(s) en catálogo.` : "No encontré libros con ese criterio.");
    return;
  }

  addMessage("agent", "No tengo una acción directa para esa frase. Prueba con: buscar libros, pedido 1, pedidos del usuario 1 o comprar 1 x 2.");
}

async function handlePrompt(text) {
  addMessage("user", text);

  try {
    await answerLocally(text);
  } catch (error) {
    setApiStatus(false, "Gateway sin respuesta");
    addMessage("agent", `No pude completar la operación. Revisa que Eureka, api-gateway, catalogue, orders y communications estén levantados. Detalle: ${error.message}`);
  }
}

els.chatForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  const text = els.messageInput.value.trim();
  if (!text) return;
  els.messageInput.value = "";
  await handlePrompt(text);
});

els.settingsForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  state.gatewayUrl = els.gatewayUrl.value.replace(/\/$/, "");
  connectWebSocket();
  try {
    await loadBooks();
    addMessage("agent", "Conexión actualizada y catálogo verificado.");
  } catch (error) {
    setApiStatus(false, "Gateway sin respuesta");
    addMessage("agent", "No pude verificar el gateway con esa URL.");
  }
});

els.clearChat.addEventListener("click", () => {
  els.messages.innerHTML = "";
  addMessage("agent", "Conversación reiniciada. ¿Qué necesita revisar el cliente?");
});

els.refreshBooks.addEventListener("click", async () => {
  try {
    const books = await loadBooks();
    addMessage("agent", `Catálogo actualizado: ${books.length} libro(s) recuperados.`);
  } catch (error) {
    setApiStatus(false, "Gateway sin respuesta");
    addMessage("agent", "No pude actualizar el catálogo. Revisa los microservicios.");
  }
});

els.orderForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  try {
    const order = await createOrder(els.bookId.value, els.quantity.value);
    addMessage("user", `Crear pedido: libro ${els.bookId.value}, cantidad ${els.quantity.value}`);
    addMessage("agent", `Pedido #${order.orderId} creado correctamente. Esta acción prueba la cadena orders -> RabbitMQ -> communications -> Gmail.`);
  } catch (error) {
    setApiStatus(false, "Gateway sin respuesta");
    addMessage("agent", `No se pudo crear el pedido. Detalle: ${error.message}`);
  }
});

document.querySelectorAll("[data-prompt]").forEach((button) => {
  button.addEventListener("click", () => handlePrompt(button.dataset.prompt));
});

addMessage("agent", "Hola, soy el agente de atención de Relatos de Papel. Puedo ayudarte a buscar libros, consultar pedidos o crear una compra demo para validar el envío asincrónico de correo.");
connectWebSocket();
loadBooks().catch(() => setApiStatus(false, "Gateway sin respuesta"));
