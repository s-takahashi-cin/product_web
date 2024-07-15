document.addEventListener("DOMContentLoaded", function () {
  function loadStores() {
    fetch("/store_info")
      .then((response) => response.json())
      .then((data) => {
        const storeContainer = document.getElementById("store-container");
        storeContainer.innerHTML = "";
        data.forEach((store) => {
          const storeItem = document.createElement("div");
          storeItem.className = "store-items";
          storeItem.innerHTML = `
                        <h2>${store.name}</h2>
                        <span>${store.address}</span>
                    `;
          storeContainer.appendChild(storeItem);
        });
      })
      .catch((error) => {
        console.error("Error fetching store data:", error);
      });
  }
  loadStores();
});
