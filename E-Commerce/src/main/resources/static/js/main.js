
var articles = [
];

var cart = {};
var templates = {
    article: '<div class="article">' +
    '<div class="image"></div>' +
    '<div class="name"></div>' +
    '<div class="description"></div>' +
    '<div class="price"></div>' +
    '<div class="number-of-units"></div>' +
    '</div>',

    cartItem: '<div class="cart-item">' +
    '<div class="name"></div>' +
    '<div class="modify-cart-wrapper">' +

    '<div class="modify-cart-button minus-button"><i class="fa fa-minus"></i></div>' +
    '<div class="number-of-items">10</div>' +
    '<div class="modify-cart-button plus-button"><i class="fa fa-plus"></i></div>' +

    '</div>' +

    '<div class="unit-price">13$</div>' +
    '<div class="total-price">130$</div>' +
    '</div>'
};

function addCartItem(id) {
    $.ajax({
        type: "POST",
        url: "/cart/add",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        data: JSON.stringify({
            "product_id": parseInt(id),
            "amount": 1
        }),
        success: function () {
            if (!cart[id]) {
                cart[id] = 0;
            }
            cart[id]++;

            refreshCart();
        }
    })

}
function removeCartItem(id) {
    $.ajax({
        type: "POST",
        url: "/cart/add",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
        },
        data: JSON.stringify({
            "product_id": parseInt(id),
            "amount": -1
        }),
        success: function () {
            cart[id]--;
            if (cart[id] === 0) {
                delete cart[id];
            }
            refreshCart();
        }
    })

}

function createArticleNode(article) {
    // We create the <template> element (so we can insert html strings into it)
    var templateElement = document.createElement('template');
    templateElement.innerHTML = templates.article.trim();

    // We set the new element's content and necessary attributes;
    var domArticle = templateElement.content.firstChild;
    if (cart.hasOwnProperty(article.id)) {
        $(domArticle).addClass('in-cart');
    }

    // ----- setting content
    $(domArticle).find('.name').text(article.name);
    $(domArticle).find('.description').text(article.description);
    $(domArticle).find('.price').text(article.price + '$');
    $(domArticle).find('.image').css('background-image', 'url(' + article.image + ')');
    if (cart[article.id] === 1) {
        $(domArticle).find('.number-of-units').text(cart[article.id] + ' unit added to cart');
    }
    else {
        $(domArticle).find('.number-of-units').text(cart[article.id] + ' units added to cart');
    }

    // ----- setting the click handler
    $(domArticle).click(function () {
        openArticleDetail(article);
    });

    // ----- setting the id
    domArticle.id = 'article-' + article.id;
    return domArticle;
}

function refreshArticles() {
    // Delete all the articles from the DOM
    $('#articles-container').empty();

    articles.forEach(function (item) {
        var domArticle = createArticleNode(item);

        // We insert the new element into the DOM
        $('#articles-container').append(domArticle);
    });
}

function createCartItemNode(itemId, number) {

    // We create the <template> element (so we can insert html strings into it)
    var templateElement = document.createElement('template');
    templateElement.innerHTML = templates.cartItem.trim();

    // We set the new element's content and necessary attributes;
    var domCartItem = templateElement.content.firstChild;
    var foundArticle = articles.find(function (x) {
        return x.id == itemId;
    });
    $(domCartItem).find('.name').text(foundArticle.name);

    $(domCartItem).find('.number-of-items').text(cart[itemId]);
    $(domCartItem).find('.unit-price').text(foundArticle.price + '$');
    $(domCartItem).find('.total-price').text(cart[itemId] * foundArticle.price + '$');

    // ----- setting the click handlers
    $(domCartItem).find('.minus-button').click(function () {
        removeCartItem(itemId);
        refreshCart();
    });
    $(domCartItem).find('.plus-button').click(function () {
        addCartItem(itemId);
        refreshCart();
    });
    $(domCartItem).find('.name').click(function () {
        openArticleDetail(foundArticle);
    })

    return domCartItem;
}

function createTotalPriceNode() {
    var cartList = $('#cart-list');
    var pricesSum = 0;
    for (var key in cart) {
        pricesSum += cart[key] * articles.find(function (x) {
                return x.id == key;
            }).price;
    }

    var templateElement = document.createElement('template');
    templateElement.innerHTML = '<div class="cart-total-price"><span>TOTAL:</span><span>' + pricesSum + '$</span></div>';

    // We set the new element's content and necessary attributes;
    var totalItem = templateElement.content.firstChild;
    cartList.append(totalItem);
    cartList.append('<br><button onclick="checkout()">Checkout</button>');
}

function refreshCart() {
    var cartList = $('#cart-list');
    cartList.empty();

    for (const key in cart) {
        var domCartItem = createCartItemNode(key, cart[key]);

        // We insert the new element into the DOM
        cartList.append(domCartItem);
    }
    createTotalPriceNode();

    refreshArticles();
}

function refreshDetailPrice(article) {
    var articleDetailContainer = $('#article-detail-container');
    var minusButton = articleDetailContainer.find('.detail-minus');
    var totalPrice = articleDetailContainer.find('.total-price');
    var plusButton = articleDetailContainer.find('.detail-plus');

    if (cart[article.id]) {
        totalPrice.addClass('shown');
        minusButton.addClass('shown');
        totalPrice.text(cart[article.id]);
    }
    else {
        totalPrice.removeClass('shown');
        minusButton.removeClass('shown');
    }
}


function openArticleDetail(article) {
    var articleDetailContainer = $('#article-detail-container');
    var minusButton = articleDetailContainer.find('.detail-minus');
    var plusButton = articleDetailContainer.find('.detail-plus');

    articleDetailContainer.addClass('shown');
    articleDetailContainer.find('.name').text(article.name);
    articleDetailContainer.find('.price').text(article.price + '$');
    articleDetailContainer.find('.description').text(article.description);
    articleDetailContainer.find('.image').css('background-image', 'url(' + article.img + ')');
    refreshDetailPrice(article);

    minusButton.bind('click', function () {
        removeCartItem(article.id);
        refreshDetailPrice(article);
    });
    plusButton.bind('click', function () {
        addCartItem(article.id);
        refreshDetailPrice(article);
    });
}
function closeArticleDetail() {
    var articleDetailContainer = $('#article-detail-container');
    var minusButton = articleDetailContainer.find('.detail-minus');
    var plusButton = articleDetailContainer.find('.detail-plus');

    minusButton.unbind('click');
    plusButton.unbind('click');
    articleDetailContainer.removeClass('shown');
}

function toggleCartList() {
    $('#cart-list').toggleClass('shown');
    $('#show-cart-button').find('i').toggleClass('fa-angle-up');
}

function checkout() {
    $.ajax({
        type: "POST",
        url: "/cart/checkout",
        success: function () {
            fetchCart();
        }
    });
}

function fetchCart() {
    $.ajax({
        type: "GET",
        url: "/cart",
        headers: {
            "Accept": "application/json"
        },
        success: function (data) {
            var cart_items = data.cart;
            cart_items.forEach(function (item) {
                cart[item.product.id] = item.quantity;
            });
            refreshCart();
            $('#article-detail-container').find('#article-detail-overlay').click(closeArticleDetail);
            $('#show-cart-button').click(toggleCartList);
        }
    })
}

$(document).ready(function () {

    $.ajax({
        type: "GET",
        url: "/products",
        headers: {
            "Accept": "application/json"
        },
        success: function (data) {
            articles = data.products;
            fetchCart()
        }
    })
});