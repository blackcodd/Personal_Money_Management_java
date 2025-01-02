     // const userId = [[${user_id}]]; // Default to '0' if not set
     //  console.log( userId);
       var userId = document.getElementById("logged-in-user-id").value;
      console.log(`UserId: ${userId}`);

      document.addEventListener('DOMContentLoaded', function() { loadAllTransactions(); });
        function loadAllTransactions()
         { fetch(`/filterTransactions?user_id=${userId}`)
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









