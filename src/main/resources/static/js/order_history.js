document.addEventListener("DOMContentLoaded", function () {
  const storeSelect = document.getElementById("storeSelect");
  const orderHistoryContainer = document.getElementById(
    "order-history-container"
  );
  const allOrdersLink = document.getElementById("allOrdersLink");

  fetch("/store_info")
    .then((response) => response.json())
    .then((stores) => {
      stores.forEach((store) => {
        const option = document.createElement("option");
        option.value = store.id;
        option.textContent = store.name;
        storeSelect.appendChild(option);
      });
    });

  const fetchOrderHistory = (storeId = "") => {
    const url = storeId
      ? `/order_history?storeId=${storeId}`
      : "/order_history";
    fetch(url)
      .then((response) => response.json())
      .then((data) => {
        orderHistoryContainer.innerHTML = ""; // Clear existing content

        if (!data.orders || data.orders.length === 0) {
          orderHistoryContainer.innerHTML = "<p>注文履歴がありません。</p>";
          return;
        }

        const table = document.createElement("table");
        table.innerHTML = `
                    <thead>
                        <tr>
                            <th>注文番号</th>
                            <th>店舗名</th>
                            <th>合計金額</th>
                            <th>注文日時</th>
                            <th>名前</th>
                            <th>詳細</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${data.orders
                          .map(
                            (order) => `
                            <tr>
                                <td>${order.orderId}</td>
                                <td>${order.storeName}</td>
                                <td>${order.totalAmount}円</td>
                                <td>${order.createdAt}</td>
                                <td>${order.lastName}</td>
                                <td><a href="/order_history_detail/${order.orderId}">詳細を表示</a></td>
                            </tr>
                        `
                          )
                          .join("")}
                    </tbody>
                `;
        orderHistoryContainer.appendChild(table);
      });
  };

  // Fetch all order history on page load
  fetchOrderHistory();

  // Filter order history by store
  storeSelect.addEventListener("change", function () {
    const storeId = storeSelect.value;
    fetchOrderHistory(storeId);
  });

  // Link to fetch all order history
  allOrdersLink.addEventListener("click", function (e) {
    e.preventDefault();
    storeSelect.value = "";
    fetchOrderHistory();
  });
});
