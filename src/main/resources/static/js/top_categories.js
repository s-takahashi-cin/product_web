document.addEventListener("DOMContentLoaded", function () {
  fetchTopCategories();
});

function fetchTopCategories() {
  fetch("/top_categories")
    .then((response) => response.json())
    .then((data) => {
      console.log("データの取得に成功しました:", data);
      var html = '<div class="grid-container">';
      data.forEach((category) => {
        html +=
          '<div class="grid-item"><a href="/mid_categories_page/' +
          category.id +
          '">' +
          category.name +
          "</a></div>";
      });
      html += "</div>";
      document.getElementById("topCategories").innerHTML = html;
    })
    .catch((error) => {
      console.error("データの取得中にエラーが発生しました:", error);
    });
}
