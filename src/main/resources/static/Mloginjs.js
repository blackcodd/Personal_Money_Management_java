document.querySelectorAll('.tab-header div').forEach(tab => {
    tab.addEventListener('click', function() {
        let targetTab = this.getAttribute('data-tab');

        // Remove active class from all tabs and contents
        document.querySelectorAll('.tab-header div').forEach(t => t.classList.remove('active'));
        document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));

        // Add active class to the clicked tab and corresponding content
        this.classList.add('active');
        document.getElementById(targetTab).classList.add('active');
    });
});