document.addEventListener("DOMContentLoaded", () => {
  const fetchProducts = async (productCategoryId, storeId) => {
    console.log("商品ページに取得しているカテゴリID:", productCategoryId);
    console.log("商品ページに取得している店舗ID:", storeId);
    try {
      const response = await fetch(
        `/products/${productCategoryId}?store_id=${storeId}`
      );

      if (!response.ok) {
        const errorText = await response.text();
        console.error(
          `Error fetching products: ${response.status} ${response.statusText}\n${errorText}`
        );
        return { products: [], store_id: storeId };
      }

      // 成功した場合の処理
      const data = await response.json();
      console.log("サーバーから取得したデータ:", data);
      return data.products;
    } catch (error) {
      console.error("Error fetching products:", error);
      return { products: [], store_id: storeId };
    }
  };

  // 商品一覧を表示する関数
  const displayProducts = (products) => {
    if (!products) {
      console.error("invalid:", products);
      return;
    }
  };

  window.openModal = function (button) {
    const productId = button.getAttribute("data-id");
    const productName = button.getAttribute("data-name");
    const productPrice = button.getAttribute("data-price");

    // 取得したデータをコンソールに表示して確認
    console.log("カートに追加する商品 ID:", productId);
    console.log("カートに追加する商品 Name:", productName);
    console.log("カートに追加する商品 Price:", productPrice);

    const modalProductDetails = document.getElementById("modalProductDetails");
    modalProductDetails.textContent = `${productName}をカートに追加しますか？`;

    const modal = document.getElementById("myModal");
    modal.style.display = "block";

    // Confirm button
    const confirmButton = document.getElementById("confirmAddToCart");
    confirmButton.onclick = function () {
      addToCart(productId, productName, productPrice);
      modal.style.display = "none";
    };
  };

  // モーダルを閉じるための関数
  const closeModalButtons = document.getElementsByClassName("close-modal");
  Array.from(closeModalButtons).forEach((button) => {
    button.onclick = function () {
      const modal = document.getElementById("myModal");
      modal.style.display = "none";
    };
  });

  // カートにアイテムを追加する関数
  function addToCart(productId, productName, productPrice) {
    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    const existingItem = cart.find((item) => item.id === productId);

    if (existingItem) {
      existingItem.quantity += 1;
    } else {
      cart.push({
        id: productId,
        name: productName,
        price: parseFloat(productPrice),
        quantity: 1,
      });
    }

    localStorage.setItem("cart", JSON.stringify(cart));
    updateCartItemCount();
  }

  // カート内のアイテム数を更新する関数
  function updateCartItemCount() {
    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    let itemCount = cart.reduce((total, item) => total + item.quantity, 0);

    const cartItemCountElement = document.getElementById("cartItemCount");
    cartItemCountElement.textContent = itemCount;
  }

  // カート内容を表示するモーダルを開く関数
  window.openCartDetails = function () {
    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    const cartItemListElement = document.getElementById("cartItemList");
    const totalPriceElement = document.getElementById("totalPrice");

    // カート内の商品一覧を作成
    cartItemListElement.innerHTML = "";
    let totalPrice = 0;
    cart.forEach((item) => {
      const itemElement = document.createElement("div");
      itemElement.textContent = `${item.name} - ¥${item.price} x ${item.quantity}`;

      // 削除ボタンを追加
      const deleteButton = document.createElement("button");
      deleteButton.textContent = "削除";
      deleteButton.onclick = () => window.removeFromCart(item.id);

      itemElement.appendChild(deleteButton);
      cartItemListElement.appendChild(itemElement);

      totalPrice += item.price * item.quantity;
    });

    totalPriceElement.textContent = `合計金額: ¥${totalPrice}`;

    const cartDetailsModal = document.getElementById("cartDetailsModal");
    cartDetailsModal.style.display = "block";
  };

  // カートモーダルを閉じるための関数
  const closeCartButtons = document.getElementsByClassName("close-cart");
  Array.from(closeCartButtons).forEach((button) => {
    button.onclick = function () {
      const cartDetailsModal = document.getElementById("cartDetailsModal");
      cartDetailsModal.style.display = "none";
    };
  });

  // カートをリセットする関数
  window.resetCart = function () {
    localStorage.removeItem("cart");
    updateCartItemCount();
    console.log("カートがリセットされました");
  };

  // カートからアイテムを削除する関数
  window.removeFromCart = function (productId) {
    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    cart = cart.filter((item) => item.id !== productId);
    localStorage.setItem("cart", JSON.stringify(cart));
    updateCartItemCount();
    openCartDetails();
  };

  // リセットボタンにイベントリスナーを追加
  const resetCartButton = document.getElementById("resetCartButton");
  resetCartButton.addEventListener("click", resetCart);

  // ページ読み込み時にカート内のアイテム数を表示
  updateCartItemCount();

  // カートアイテム数をクリックしたときにカート内容を表示
  document
    .getElementById("cartItemCount")
    .addEventListener("click", openCartDetails);
});
