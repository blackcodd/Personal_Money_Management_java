function toggleDropdown() {
    const dropdownMenu = document.getElementById('dropdownMenu');
    dropdownMenu.style.display = dropdownMenu.style.display === 'block' ? 'none' : 'block';
}

// ড্রপডাউনের বাইরে ক্লিক করলে এটি বন্ধ হবে
window.onclick = function(event) {
    if (!event.target.matches('.profile-button') && !event.target.matches('.profile-button *')) {
        const dropdownMenu = document.getElementById('dropdownMenu');
        if (dropdownMenu.style.display === 'block') {
            dropdownMenu.style.display = 'none';
        }
    }
};


//download er jonno

async function downloadReport() {
    // Import jsPDF and autoTable plugins
    const { jsPDF } = window.jspdf;

    // Create a new PDF document
    const doc = new jsPDF();

    // Add a title to the PDF
    doc.setFontSize(18);
    doc.text("Filtered Financial Report", 14, 20);

    // Table Headers
    const headers = ['Date', 'Type', 'Category', 'Amount', 'Media'];

    // Gather table data
    const rows = [];
    const tableRows = document.querySelectorAll('#report-table-body tr');

    tableRows.forEach(row => {
        const cells = row.querySelectorAll('td');
        if (cells.length > 0 && row.style.display !== 'none') { // Ensure the row is visible
            rows.push([
                cells[0].innerText || '', // Date
                cells[1].innerText || '', // Type
                cells[2].innerText || '', // Category
                cells[3].innerText || '', // Amount
                cells[4].innerText || ''  // Media
            ]);
        }
    });

    if (rows.length === 0) {
        doc.text("No data to display based on your filters.", 14, 40);
    } else {
        // Add table data to the PDF using autoTable
        doc.autoTable({
            head: [headers],
            body: rows,
            startY: 30,
            theme: 'grid',
            margin: { top: 30 },
            styles: { fontSize: 10 },
        });
    }

    // Save the PDF file
    const fileName = "filtered-financial-report.pdf";
    doc.save(fileName);

    // Show a notification
    notifyUser("PDF Download Complete", `Your file "${fileName}" has been successfully saved.`);
}

// Function to display notifications
function notifyUser(title, message) {
    // Fallback to alert if notifications are not supported
    if (!("Notification" in window)) {
        alert(`${title}: ${message}`);
        return;
    }

    // Request notification permission if needed
    if (Notification.permission === "default" || Notification.permission === "denied") {
        Notification.requestPermission().then(permission => {
            if (permission === "granted") {
                new Notification(title, { body: message });
            } else {
                alert(`${title}: ${message}`);
            }
        });
    } else if (Notification.permission === "granted") {
        new Notification(title, { body: message });
    } else {
        // Fallback to alert if notifications are blocked
        alert(`${title}: ${message}`);
    }
}


// Function to change currency
function changeCurrency() {
    const currencySelect = document.getElementById("currency");
    const selectedCurrency = currencySelect.value;

    // Display a confirmation message or apply currency setting logic
    alert(`Currency has been changed to: ${selectedCurrency}`);
    // You can update your app's currency format here based on the selectedCurrency
}

// Function to change theme
function changeTheme() {
    const themeSelect = document.getElementById("theme");
    const selectedTheme = themeSelect.value;

    // Apply the selected theme
    if (selectedTheme === "dark") {
        document.body.style.backgroundColor = "#333";

        document.body.style.color = "#fff";


    } else {
        document.body.style.backgroundColor = "#fff";
        document.body.style.color = "#000";
    }

    alert(`Theme has been changed to: ${selectedTheme}`);
}

// Function to change language
function changeLanguage() {
    const languageSelect = document.getElementById("language");
    const selectedLanguage = languageSelect.value;

    // Show a message or update the app's language setting
    alert(`Language has been changed to: ${selectedLanguage}`);

    // Logic for applying language changes
    if (selectedLanguage === "bn") {
        alert("বাংলা ভাষা নির্বাচিত হয়েছে।");
        // Update text elements on the page to Bangla (requires a dictionary or translation logic)
    } else if (selectedLanguage === "es") {
        alert("Idioma cambiado a Español.");
        // Update text elements on the page to Spanish
    } else if (selectedLanguage === "fr") {
        alert("Langue changée en Français.");
        // Update text elements on the page to French
    } else {
        alert("Language set to English.");
        // Default to English
    }
}
