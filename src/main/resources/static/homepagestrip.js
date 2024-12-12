let transactions = [];

    let currency = "৳";
let myChart;

// Global Variables to store amounts
let totalIncome = 0;
let totalExpenses = 0;
let currentSavings = 0;

// Function to convert amount based on selected currency
function convertCurrency(amount, currency) {
  let conversionRates = {
    'USD': 1, // Base currency
    'TAKA': 106, // Example rate
    'EUR': 0.92,
    'INR': 82,
    'GBP': 0.82
  };

  return (amount * conversionRates[currency]).toFixed(2);
}

// Function to get currency symbol
function getCurrencySymbol(currency) {
  const symbols = {
    'USD': '$',
    'TAKA': '৳',
    'EUR': '€',
    'INR': '₹',
    'GBP': '£'
  };
  return symbols[currency] || '৳';  // Default to $ if no match
}

// Function to change currency
function changeCurrency() {
  const selectedCurrency = document.getElementById("currency").value;

  // Get the correct currency symbol
  const currencySymbol = getCurrencySymbol(selectedCurrency);

  // Update the displayed values based on the selected currency
  document.getElementById("total-income").textContent = `${currencySymbol} ${convertCurrency(totalIncome, selectedCurrency)}`;
  document.getElementById("total-expenses").textContent = `${currencySymbol} ${convertCurrency(totalExpenses, selectedCurrency)}`;
  document.getElementById("current-savings").textContent = `${currencySymbol} ${convertCurrency(currentSavings, selectedCurrency)}`;

  // Optionally, update chart as well if you want to reflect currency change there
  // updateChart(selectedCurrency);
}

// Initial setup to update the dashboard when page loads
document.addEventListener("DOMContentLoaded", function() {
  // Default currency
  changeCurrency();
});


    // Function to update the Dashboard
    const updateDashboard = () => {
      document.getElementById('total-income').innerText = `${currency} ${totalIncome.toFixed(2)}`;
      document.getElementById('total-expenses').innerText = `${currency} ${totalExpenses.toFixed(2)}`;
      document.getElementById('current-savings').innerText = `${currency} ${currentSavings.toFixed(2)}`;

      // Calculate total spent and remaining budget
      totalBudgetSpent = transactions.filter(tx => tx.type === 'expense').reduce((sum, tx) => sum + tx.amount, 0);
      document.getElementById('remaining-budget').innerText = `Remaining Budget: $${(budget - totalBudgetSpent).toFixed(2)}`;
      document.getElementById('goal-status').innerText = `Goal Status: $${(financialGoal - currentSavings).toFixed(2)} remaining`;

      updateReportTable();
    };

    // Function to add a new transaction
    const addTransaction = () => {
      const amount = parseFloat(document.getElementById('amount').value);
      const type = document.getElementById('type').value;
      const category = document.getElementById('category').value;
      const date = document.getElementById('date').value;

      if (isNaN(amount) || amount <= 0 || !date.trim()) {
        alert('Please enter valid transaction details.');
        return;
      }

      const transaction = { amount, type, category, date };
      transactions.push(transaction);

      if (type === 'income') {
        totalIncome += amount;
        currentSavings += amount;
      } else {
        totalExpenses += amount;
        currentSavings -= amount;
      }

      // Update the dashboard and chart after adding transaction
      updateDashboard();
      updateChart();
      document.getElementById('amount').value = ''; // Clear the input field
    };

    // Function to update the chart
    const updateChart = () => {
      const ctx = document.getElementById('income-expense-savings-chart').getContext('2d');

      if (myChart) {
        myChart.destroy(); // Destroy the previous chart if exists
      }

const incomeChartDiv = document.querySelector('.income-chart');
incomeChartDiv.style.backgroundColor = '#f9f9f9';


      myChart = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: ['Income', 'Expenses', 'Savings'],
          datasets: [{
            label: 'Amount',
            data: [totalIncome, totalExpenses, currentSavings],
            backgroundColor: ['#36D1DC', '#FF6F61', '#5B86E5'],
            borderColor: ['#36D1DC', '#FF6F61', '#5B86E5'],
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          plugins: {
            legend: {
              display: false
            }
          },
          scales: {
            y: {
              beginAtZero: true,
              title: {
                display: true,
                text: 'Amount'
              }
            },
            x: {
              title: {
                display: true,
                text: 'Category'
              }
            }
          }
        }
      });
    };

    // Financial Goal and Budget Variables
    let financialGoal = 0;
    let budget = 0;
    let totalBudgetSpent = 0;

    // Function to update Financial Goal
    const updateGoal = () => {
      financialGoal = parseFloat(document.getElementById('financial-goal').value);
      if (isNaN(financialGoal) || financialGoal <= 0) {
        document.getElementById('goal-status').innerText = 'Goal Status: Not Set';
      } else {
        document.getElementById('goal-status').innerText = `Goal Status: $${financialGoal.toFixed(2)}`;
      }
      updateDashboard();
    };

    // Function to update Budget
    const updateBudget = () => {
      budget = parseFloat(document.getElementById('budget').value);
      if (isNaN(budget) || budget <= 0) {
        document.getElementById('remaining-budget').innerText = 'Remaining Budget: Not Set';
      } else {
        document.getElementById('remaining-budget').innerText = `Remaining Budget: $${(budget - totalBudgetSpent).toFixed(2)}`;
      }
      updateDashboard();
    };

    // Function to update the Report Table
    const updateReportTable = () => {
      const tableBody = document.getElementById('report-table-body');
      tableBody.innerHTML = ''; // Clear existing rows

      if (transactions.length === 0) {
        const noDataRow = `<tr><td colspan="4" style="text-align: center;">No transactions added yet.</td></tr>`;
        tableBody.innerHTML = noDataRow;
        return;
      }

      // Populate table rows with transactions
      transactions.forEach((tx) => {
        const row = `
      <tr>
        <td>${tx.date}</td>
        <td>${tx.type}</td>
        <td>${tx.category}</td>
        <td>${currency} ${tx.amount.toFixed(2)}</td>
      </tr>
    `;
        tableBody.innerHTML += row;
      });
    };

    // Function to download the report
    const downloadReport = () => {
      if (transactions.length === 0) {
        alert('No transactions available to download!');
        return;
      }

      // Create report content
      const reportData = transactions
        .map(
          (tx) =>
            `Date: ${tx.date}, Type: ${tx.type}, Category: ${tx.category}, Amount: ${currency} ${tx.amount.toFixed(2)}`
        )
        .join('\n');

      // Create and trigger file download
      const blob = new Blob([reportData], { type: 'text/plain' });
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = 'transactions_report.txt';
      link.click();
    };

    // Function to toggle notifications
    /*const toggleNotifications = () => {
      const isEnabled = document.getElementById('notifications').checked;
      if (isEnabled) {
        alert("Notifications Enabled");
      } else {
        alert("Notifications Disabled");
      }
    };*/

    // Function to change theme

    // Function to handle currency change
    function changeCurrency() {
      const currency = document.getElementById('currency').value;
      alert(`Currency changed to: ${currency}`);

      // Here, you could update the user's selected currency in the backend or local storage.
      // For example: localStorage.setItem('currency', currency);
    }

    // Function to handle theme change
    function changeTheme() {
      const theme = document.getElementById('theme').value;
      if (theme === 'dark') {
        document.body.style.backgroundColor = '#121212';
        document.body.style.color = '#ffffff';
        // Save theme preference in localStorage or sessionStorage
        localStorage.setItem('theme', 'dark');
      } else {
        document.body.style.backgroundColor = '#ffffff';
        document.body.style.color = '#000000';
        // Save theme preference in localStorage or sessionStorage
        localStorage.setItem('theme', 'light');
      }
    }

    // Function to toggle notifications
    function toggleNotifications() {
      const notificationsEnabled = document.getElementById('notifications').checked;
      if (notificationsEnabled) {
        alert("Notifications enabled.");
      } else {
        alert("Notifications disabled.");
      }
      // Store the user's notification preference (for example in local storage or database)
      localStorage.setItem('notifications', notificationsEnabled);
    }

    // Function to handle language change
    function changeLanguage() {
      const language = document.getElementById('language').value;

      // For demonstration, alerting the selected language.
      // In a real-world scenario, you might load translated content based on the selected language.
      alert(`Language changed to: ${language}`);

      // You can set the language in localStorage, or implement a language file switcher.
      localStorage.setItem('language', language);
    }

    // On page load, apply previously saved settings (if any)
    document.addEventListener('DOMContentLoaded', function () {
      const savedCurrency = localStorage.getItem('currency');
      const savedTheme = localStorage.getItem('theme');
      const savedNotifications = localStorage.getItem('notifications');
      const savedLanguage = localStorage.getItem('language');

      if (savedCurrency) {
        document.getElementById('currency').value = savedCurrency;
      }
      if (savedTheme) {
        document.getElementById('theme').value = savedTheme;
        changeTheme();  // Apply saved theme
      }
      if (savedNotifications !== null) {
        document.getElementById('notifications').checked = JSON.parse(savedNotifications);
      }
      if (savedLanguage) {
        document.getElementById('language').value = savedLanguage;
      }
    });


    // Initial Calls to update the dashboard and chart
    updateDashboard();
    updateChart();
