let myChart;
let incomePieChart;
let expensePieChart;

function fetchContent(url) {
    fetch(url)
        .then(response => response.text())
        .then(data => {
            document.getElementById('content-container').innerHTML = data;

            updateActiveNavLink(url);
            // Reinitialize necessary scripts for the loaded content
            if (url.includes('/dashboard')) {
                loadDashboardData();
                bindDashboardEventListeners();
                loadAllTransactions();
            }
            if(url.includes('/report')){
            loadAllTransactions();}
        })
        .catch(error => console.error('Error fetching content:', error));
}
function updateActiveNavLink(url) {
    const navLinks = document.querySelectorAll('#navigation a');

    // Remove the active class from all links
    navLinks.forEach(link => link.classList.remove('active'));

    // Normalize and match the URL path
    const urlPath = new URL(url, window.location.origin).pathname;

    // Add the active class to the matching link
    navLinks.forEach(link => {
        const linkPath = new URL(link.href, window.location.origin).pathname;
        if (linkPath === urlPath) {
            link.classList.add('active');
        }
    });
}

document.addEventListener('DOMContentLoaded', function () {
    const userId = document.getElementById('logged-in-user-id').value;
    console.log("nav user: " + userId);

    // Load the dashboard by default on first load
    const defaultUrl = new URL('/dashboard', window.location.origin);
    defaultUrl.searchParams.set('user_id', userId);
    fetchContent(defaultUrl.toString());

    updateActiveNavLink(defaultUrl.toString());
    // Define the base URLs for the navigation links
    const baseUrls = {
        "Dashboard": "/dashboard",
        "Income & Expense": "/income-expense",
        "Budget Planner": "/budget",
        "Financial Goals": "/goal",
        "Reports": "/report",
        "Settings": "/settings"
    };

    // Handle navigation link clicks
    const navLinks = document.querySelectorAll('#navigation a');
    navLinks.forEach(function (link) {
        link.addEventListener('click', function (event) {
            event.preventDefault();
            const linkText = link.textContent.trim();
            const baseUrl = baseUrls[linkText];
            if (baseUrl) {
                const url = new URL(baseUrl, window.location.origin);
                url.searchParams.set('user_id', userId);
                fetchContent(url.toString());
            }
        });
    });
});

function bindDashboardEventListeners() {
    const userId =  Number( document.getElementById('logged-in-user-id').value);
    console.log( "binduser=",userId);

    const yearFilter = document.getElementById('Dasboard-filter-year');
    const monthFilter = document.getElementById('Dasboard-filter-month');
    const dateFilter = document.getElementById('Dasboard-filter-date');

    if (yearFilter) {
        yearFilter.addEventListener('input', function () {
            filterDashboard(userId);
        });
    }
    if (monthFilter) {
        monthFilter.addEventListener('input', function () {
            filterDashboard(userId);
        });
    }
    if (dateFilter) {
        dateFilter.addEventListener('input', function () {
            filterDashboard(userId);
        });
    }
}

function loadDashboardData() {
    const userId =  Number(document.getElementById('logged-in-user-id').value);
    console.log("userid holo loaddashboard"+ userId);

    fetch(`/totals?user_id=${userId}`)
        .then(response => response.json())
        .then(data => {
            const savings = data.totalIncome - data.totalExpense;
            document.getElementById('total-income').innerText = `৳${data.totalIncome}`;
            document.getElementById('total-expenses').innerText = `৳${data.totalExpense}`;
            document.getElementById('current-savings').innerText = `৳${savings}`;
            updateChart(data.totalIncome, data.totalExpense, savings);
        })
        .catch(error => console.error('Error fetching totals:', error));

    fetch(`/category-percentages?user_id=${userId}`)
        .then(response => response.json())
        .then(data => filterPieChart(data))
        .catch(error => console.error('Error fetching category percentages:', error));
}

function filterDashboard(userId) {
console.log("filter  id ",userId);
    const year = document.getElementById('Dasboard-filter-year').value || '';
    const month = document.getElementById('Dasboard-filter-month').value || '';
    const date = document.getElementById('Dasboard-filter-date').value || '';

    fetch(`/totals?user_id=${userId}&year=${year}&month=${month}&date=${date}`)
        .then(response => response.json())
        .then(data => {
            const savings = data.totalIncome - data.totalExpense;
            document.getElementById('total-income').innerText = `৳${data.totalIncome}`;
            document.getElementById('total-expenses').innerText = `৳${data.totalExpense}`;
            document.getElementById('current-savings').innerText = `৳${savings}`;
            updateChart(data.totalIncome, data.totalExpense, savings);
        })
        .catch(error => console.error('Error filtering totals:', error));

    fetch(`/category-percentages?user_id=${userId}&year=${year}&month=${month}&date=${date}`)
        .then(response => response.json())
        .then(data => filterPieChart(data))
        .catch(error => console.error('Error filtering category percentages:', error));
}

function updateChart(totalIncome, totalExpenses, currentSavings) {
    const canvas = document.getElementById('income-expense-savings-chart');
    if (!canvas) {
        console.error('Income-expense-savings chart canvas not found.');
        return;
    }

    const ctx = canvas.getContext('2d');
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
}

function filterPieChart(data) {
    const incomeData = data.filter(item => item.type === "income");
    const expenseData = data.filter(item => item.type === "expense");

    createOrUpdatePieChart('incomeChart', incomeData, 'Income Breakdown', incomePieChart, chart => (incomePieChart = chart));
    createOrUpdatePieChart('expenseChart', expenseData, 'Expense Breakdown', expensePieChart, chart => (expensePieChart = chart));
}

function createOrUpdatePieChart(canvasId, data, label, chartInstance, setChartInstance) {
    const canvas = document.getElementById(canvasId);
    if (!canvas) {
        console.error(`Canvas with ID "${canvasId}" not found.`);
        return;
    }

    const ctx = canvas.getContext('2d');
    if (chartInstance) {
        chartInstance.destroy();
    }

    const newChart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: data.map(item => item.category),
            datasets: [{
                data: data.map(item => item.percentage),
                backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40']
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top'
                },
                title: {
                    display: true,
                    text: label
                }
            }
        }
    });

    setChartInstance(newChart);
}



    //  document.addEventListener('DOMContentLoaded', function() { loadAllTransactions(); });
        function loadAllTransactions()
         {
          var userId = document.getElementById("logged-in-user-id").value;
               console.log(`UserId: ${userId}`);
         fetch(`/filterTransactions?user_id=${userId}`)
          .then(response => response.json())
           .then(data => { const tbody = document.getElementById('report-table-body');
           tbody.innerHTML = '';
           data.forEach(transaction =>
            { const row = document.createElement('tr');
            row.innerHTML = ` <td>${transaction.date}</td>
            <td>${transaction.type}
            </td> <td>${transaction.category}
            </td> <td>${transaction.amount}</td>
             <td>${transaction.media}</td> `;
             tbody.appendChild(row);
             });
              })
              .catch((error) => { console.error('There was a problem with your fetch operation:', error); });
              }
               function showFilters() {
                          const filters = document.getElementById('filters');
                          const submitBtn = document.getElementById('submit-btn');
                          filters.style.display = 'flex';
                          submitBtn.style.display = 'block';
                      }

                function TshowFilters() {
                   const Tfilters = document.getElementById('table-filters');
                                                const TsubmitBtn = document.getElementById('Tsubmit-btn');
                                                Tfilters.style.display = 'flex';
                                                TsubmitBtn.style.display = 'block';
                                            }



        function filterTable(){
            const year = document.getElementById('filter-year').value;
            const month = document.getElementById('filter-month').value;
            const date = document.getElementById('filter-date').value;

            fetch(`/filterTransactions?user_id=${userId}&year=${year}&month=${month}&date=${date}`)
                .then(response => response.json())
                .then(data => {
                    const tbody = document.getElementById('report-table-body');
                    tbody.innerHTML = '';
                    data.forEach(transaction => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>${transaction.date}</td>
                            <td>${transaction.type}</td>
                            <td>${transaction.category}</td>
                            <td>${transaction.amount}</td>
                            <td>${transaction.media}</td>
                        `;
                        tbody.appendChild(row);
                    });
                });
        }


