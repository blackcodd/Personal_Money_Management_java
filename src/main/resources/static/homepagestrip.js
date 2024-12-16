// Define global variables
let transactions = [];
let currency = "৳";
let myChart;

// Global variables for storing amounts
let totalIncome = 0;
let totalExpenses = 0;
let currentSavings = 0;

// Function to fetch data from the database and update the graph
async function fetchDataAndUpdateChart() {
    try {
        // Fetch income and expense data from the backend
        const incomeResponse = await fetch('/api/income');
        const expenseResponse = await fetch('/api/expense');

        const incomeData = await incomeResponse.json();
        const expenseData = await expenseResponse.json();

        // Process income data
        totalIncome = incomeData.reduce((sum, item) => sum + item.amount, 0);

        // Process expense data
        totalExpenses = expenseData.reduce((sum, item) => sum + item.amount, 0);

        // Calculate current savings
        currentSavings = totalIncome - totalExpenses;

        // Update the chart with new data
        updateChart();

        // Update the dashboard with new totals
        updateDashboard();
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}

// Function to send data to the backend
async function addTransactionToDatabase(transaction) {
    const endpoint = transaction.type === 'income' ? '/api/income' : '/api/expense';

    try {
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(transaction),
        });

        if (response.ok) {
            console.log('Transaction added successfully');
            fetchDataAndUpdateChart(); // Refresh data after adding
        } else {
            console.error('Failed to add transaction');
        }
    } catch (error) {
        console.error('Error adding transaction:', error);
    }
}

// Function to update the dashboard
function updateDashboard() {
    document.getElementById('total-income').innerText = `${currency} ${totalIncome.toFixed(2)}`;
    document.getElementById('total-expenses').innerText = `${currency} ${totalExpenses.toFixed(2)}`;
    document.getElementById('current-savings').innerText = `${currency} ${currentSavings.toFixed(2)}`;
}

// Function to update the chart
function updateChart() {
    const ctx = document.getElementById('income-expense-savings-chart').getContext('2d');

    if (myChart) {
        myChart.destroy();
    }

    myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Income', 'Expenses', 'Savings'],
            datasets: [{
                label: 'Amount',
                data: [totalIncome, totalExpenses, currentSavings],
                backgroundColor: ['#36D1DC', '#FF6F61', '#5B86E5'],
                borderColor: ['#36D1DC', '#FF6F61', '#5B86E5'],
                borderWidth: 1,
            }],
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    display: false,
                },
            },
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Amount',
                    },
                },
                x: {
                    title: {
                        display: true,
                        text: 'Category',
                    },
                },
            },
        },
    });
}

// Function to handle adding a new transaction
function addTransaction() {
    const amount = parseFloat(document.getElementById('amount').value);
    const type = document.getElementById('type').value;
    const category = document.getElementById('category').value;
    const media = document.getElementById('media').value;
    const time = document.getElementById('time').value;

    if (isNaN(amount) || amount <= 0 || !time.trim()) {
        alert('Please enter valid transaction details.');
        return;
    }

    const transaction = { amount, type, category, media, time };

    // Send the transaction to the database
    addTransactionToDatabase(transaction);

    // Clear input fields
    document.getElementById('amount').value = '';
}

// Event listener for page load
document.addEventListener('DOMContentLoaded', function () {
    fetchDataAndUpdateChart(); // Fetch data and update chart on load
});
