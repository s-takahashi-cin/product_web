document.addEventListener("DOMContentLoaded", function () {
  console.log("DOMContentLoadedイベントが発生しました");
  fetchMidCategories();
});

function fetchMidCategories() {
  const categoryId = getCategoryIdFromPath();
  console.log("取得したカテゴリID:", categoryId);

  if (!categoryId) {
    console.error("カテゴリIDが指定されていません");
    return;
  }

  fetch(`/mid_categories/${categoryId}`)
    .then((response) => {
      if (!response.ok) {
        throw new Error("レスポンスがエラーを返しました: " + response.status);
      }
      return response.json();
    })
    .then((data) => {
      console.log("中カテゴリデータの取得に成功しました:", data);
      var html = '<div class="grid-container">';
      data.forEach((midCategory) => {
        html +=
          '<div class="grid-item"><a href="/low_categories_page/' +
          midCategory.id +
          '">' +
          midCategory.name +
          "</a></div>";
      });
      html += "</div>";
      document.getElementById("midCategories").innerHTML = html;
    })
    .catch((error) => {
      console.error("データの取得中にエラーが発生しました:", error);
    });
}

function getCategoryIdFromPath() {
  const pathParts = window.location.pathname.split("/");
  return pathParts[pathParts.length - 1];
}
