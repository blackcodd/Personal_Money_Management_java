document.querySelector(".submit-btn").addEventListener("click", function (event) {
    event.preventDefault(); // Prevent default form submission behavior

    // Fetch user input
    const startDate = document.getElementById("startDate").value;
    const endDate = document.getElementById("endDate").value;
    const budgetAmount = parseFloat(document.getElementById("budgetAmount").value);
    const userId = document.getElementById("logged-in-user-id").value;

    // Validate inputs
    if (!startDate || !endDate || !budgetAmount || !userId) {
        alert("Please fill all the fields and ensure you are logged in.");
        return;
    }

    // Check if a budget exists, delete it if necessary, then set the new one
    checkAndDeleteBudget(userId)
        .then(() => {
            // Prepare payload
            const payload = new URLSearchParams();
            payload.append("id", userId);
            payload.append("start_date", startDate);
            payload.append("end_date", endDate);
            payload.append("amount", budgetAmount);

            // Send POST request to set the budget
            return fetch("/budget/setbudget", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: payload,
            });
        })
        .then(response => response.ok ? response.text() : response.text().then(text => { throw new Error(text); }))
        .then(data => {
            alert(data);
            const budgetDetails = { startDate, endDate, budgetAmount, userId };
            localStorage.setItem("budgetDetails", JSON.stringify(budgetDetails));
            startExpenseMonitoring();
        })
        .catch(error => {
            alert("Error: " + error.message);
        });
});

// Function to check if a budget exists and delete it if necessary
function checkAndDeleteBudget(userId) {
    return fetch(`/budget/check?user_id=${userId}`, {
        method: "GET",
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to check for existing budget.");
        }
        return response.json();
    })
    .then(data => {
        if (data.exists) {
            console.log("Budget exists. Deleting...");
            return deletePreviousBudget(userId);
        } else {
            console.log("No budget exists. Proceeding to insert a new one.");
            return Promise.resolve(); // Nothing to delete
        }
    })
    .catch(error => {
        console.error("Error checking for existing budget:", error);
        throw error; // Rethrow the error to handle it in the main flow
    });
}

// Function to delete the previous budget for a user
function deletePreviousBudget(userId) {
    return fetch(`/budget/delete?user_id=${userId}`, {
        method: "DELETE",
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text); });
        }
        console.log(`Previous budget for user ID ${userId} deleted successfully.`);
    })
    .catch(error => {
        console.error("Error deleting previous budget:", error);
        throw error; // Rethrow the error to handle it in the main flow
    });
}

// Function to monitor expenses
function startExpenseMonitoring() {
    const budgetDetails = JSON.parse(localStorage.getItem("budgetDetails"));
    if (!budgetDetails) return;

    const { startDate, endDate, budgetAmount, userId } = budgetDetails;
    clearExistingMonitoring();

    const intervalId = setInterval(() => {
        const now = new Date();
        const end = new Date(endDate);

        if (now > end) {
            const remainingBudget = calculateRemainingBudget(userId, startDate, endDate, budgetAmount, true);
            clearInterval(intervalId);
            return;
        }

        calculateRemainingBudget(userId, startDate, endDate, budgetAmount, false);
    }, 60000); // Check every 60 seconds

    localStorage.setItem("expenseMonitoringInterval", intervalId);
}

// Other functions (calculateRemainingBudget, clearExistingMonitoring, notifyUserToSetBudget) remain unchanged.
