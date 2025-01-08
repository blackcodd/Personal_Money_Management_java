    var userId = document.getElementById("logged-in-user-id").value;
            console.log(`chart UserId: ${userId}`);
            let myChart;
            let incomePieChart;
            let expensePieChart;
   document.addEventListener('DOMContentLoaded', function() {loadDashboardData();}); // Load default values for the last month

   document.getElementById('Dasboard-filter-year').addEventListener('input', function() {
       filterDashboard(userId);
   });
   document.getElementById('Dasboard-filter-month').addEventListener('input', function() {
       filterDashboard(userId);
   });
   document.getElementById('Dasboard-filter-date').addEventListener('input', function() {
       filterDashboard(userId);
   });

function loadDashboardData() {
   fetch(`/totals?user_id=${userId}`)
       .then(response => response.json())
       .then(data => {
       const savings=data.totalIncome-data.totalExpense;
           document.getElementById('total-income').innerText = `৳${data.totalIncome}`;
           document.getElementById('total-expenses').innerText = `৳${data.totalExpense}`;
           document.getElementById('current-savings').innerText = `৳${savings}`;
          updateChart(data.totalIncome, data.totalExpense,savings);
       })
       .catch((error) => {
           console.error('There was a problem with your fetch operation:', error);
       });
        fetch(`/category-percentages?user_id=${userId}`)
                      .then(response => response.json())
                      .then(data => {
                          filterPieChart(data);
                      })
                      .catch(error => console.error('Error fetching filtered pie chart data:', error));

}

function filterDashboard(userId) {
   const year = document.getElementById('Dasboard-filter-year').value;
   const month = document.getElementById('Dasboard-filter-month').value;
   const date = document.getElementById('Dasboard-filter-date').value;
   fetch(`/totals?user_id=${userId}&year=${year}&month=${month}&date=${date}`)
       .then(response => response.json())
       .then(data => {
            const savings=data.totalIncome-data.totalExpense;
           document.getElementById('total-income').innerText = `৳${data.totalIncome}`;
           document.getElementById('total-expenses').innerText = `৳${data.totalExpense}`;
           document.getElementById('current-savings').innerText = `৳${savings}`;
           updateChart(data.totalIncome, data.totalExpense, savings);

       })
       .catch((error) => {
           console.error('There was a problem with your fetch operation:', error);
       });


       fetch(`/category-percentages?user_id=${userId}&year=${year}&month=${month}&date=${date}`)
              .then(response => response.json())
              .then(data => {
              filterPieChart(data);


              })
              .catch((error) => {
                  console.error('There was a problem with your fetch operation:', error);
              });

}



    function updateChart(totalIncome,totalExpenses,currentSavings) {
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

function filterPieChart(data) {
console.log('Pie chart data:', data);
    const incomeData = data.filter(item => item.type === "income");
    const expenseData = data.filter(item => item.type === "expense");

    createOrUpdatePieChart('incomeChart', incomeData, 'Income Breakdown', incomePieChart, chart => (incomePieChart = chart));
    createOrUpdatePieChart('expenseChart', expenseData, 'Expense Breakdown', expensePieChart, chart => (expensePieChart = chart));
}
function createOrUpdatePieChart(canvasId, data, label, chartInstance, setChartInstance) {
    const ctx = document.getElementById(canvasId).getContext('2d');
    if (!ctx) {
        console.error(`Canvas with ID "${canvasId}" not found.`);
        return;
    }
    if (chartInstance) {
        chartInstance.destroy();
    }

    // Generate dynamic colors for each category
    const backgroundColors = data.map(() => `#${Math.floor(Math.random()*16777215).toString(16)}`);

    const newChart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: data.map(item => item.category),
            datasets: [{
                data: data.map(item => item.percentage.toFixed(2)),
                backgroundColor: backgroundColors,
            }],
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: label,
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            const label = context.label || '';
                            const value = context.raw || '';
                            return `${value}%`;
                        }
                    }
                },
                datalabels: {
                    color: '#00000',
                    formatter: (value) => {
                        return `${value}%`;
                    },
                    font: {
                        weight: 'bold'
                    },
                  // align: 'end',
                   // anchor: 'end',
                    clamp: true, // Ensure labels are within the segment boundaries
                    offset: 10 // Adjust to increase spacing of labels from segments
                }
            },
        },
        plugins: [ChartDataLabels],
    });

    setChartInstance(newChart);






//function createOrUpdatePieChart(canvasId, data, label, chartInstance, setChartInstance) {
//    const ctx = document.getElementById(canvasId).getContext('2d');
//    if (!ctx) {
//           console.error(`Canvas with ID "${canvasId}" not found.`);
//           return;
//       }
//    if (chartInstance) {
//        chartInstance.destroy();
//    }
//// this is the chirt
//
//    const newChart = new Chart(ctx, {
//        type: 'pie',
//        data: {
//            labels: data.map(item => item.category),
//            datasets: [{
//                data: data.map(item => item.percentage),
//                backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40'], // Adjust colors as needed
//            }],
//        },
//        options: {
//            responsive: true,
//            plugins: {
//                legend: {
//                    position: 'top',
//                },
//                title: {
//                    display: true,
//                    text: label,
//                },
//            },
//        },
//    });
//
//    setChartInstance(newChart);
}