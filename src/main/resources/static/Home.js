function checkCategory() {
    var category = document.getElementById("category").value;
    var otherCategoryInput = document.getElementById("other-category");
    if (category === "others") {
        otherCategoryInput.style.display = "block";
    } else {
        otherCategoryInput.style.display = "none";
    }
}

function checkMedia() {
    var media = document.getElementById("media").value;
    var otherMedia = document.getElementById("other_media");
    if (media === "other") {
        otherMedia.style.display = "block";
    } else {
        otherMedia.style.display = "none";
    }
}

function addTransaction() {
    var amount = document.getElementById("amount").value;
    var type = document.getElementById("type").value;
    var media = document.getElementById("media").value;
    var category = document.getElementById("category").value;
    var date = document.getElementById("date").value;
    var userId = document.getElementById("logged-in-user-id").value;
    var otherCategoryInput = document.getElementById("other-category").value;
    var otherMedia = document.getElementById("other_media").value;

    if (media === "other") {
        media = otherMedia;
    }
    if (category === "others") {
        category = otherCategoryInput;
    }

    var transaction = {
        amount: amount,
        media: media,
        category: category,
        date: date
    };

    var url = (type === "income") ? `/api/incomes?userId=${userId}` : `/api/expenses?userId=${userId}`;

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(transaction),
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        alert(type.charAt(0).toUpperCase() + type.slice(1) + " added successfully!");
        window.location.reload();
    })
    .catch((error) => {
        console.error('There was a problem with your fetch operation:', error);
    });
}
