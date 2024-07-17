document.addEventListener("DOMContentLoaded", function () {
  fetchLowCategories();
});

function fetchLowCategories() {
  const subCategoryId = getSubCategoryIdFromPath();

  if (!subCategoryId) {
    console.error("サブカテゴリIDが指定されていません");
    return;
  }

  fetch(`/low_categories/${subCategoryId}`)
    .then((response) => {
      if (!response.ok) {
        throw new Error("レスポンスがエラーを返しました: " + response.status);
      }
      return response.json();
    })
    .then((data) => {
      var html = '<div class="grid-container">';
      data.forEach((lowCategory) => {
        html +=
          '<div class="grid-item"><a href="/products_page/' +
          lowCategory.id +
          '">' +
          lowCategory.name +
          "</a></div>";
      });
      html += "</div>";
      document.getElementById("lowCategories").innerHTML = html;
    })
    .catch((error) => {
      console.error("データの取得中にエラーが発生しました:", error);
    });
}

function getSubCategoryIdFromPath() {
  const pathParts = window.location.pathname.split("/");
  return pathParts[pathParts.length - 1];
}
