// Interactive components and layout initialization
document.addEventListener('DOMContentLoaded', () => {
    
    // Auto-dismiss alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            alert.style.transition = 'opacity 0.5s ease';
            setTimeout(() => alert.remove(), 500);
        }, 5000);
    });

    // Mobile menu toggle
    const createMobileMenu = () => {
        const nav = document.querySelector('.navbar .nav-links');
        if (!nav) return;
        
        // This is a simple implementation, would be expanded for fully responsive mobile view
    };
    
    createMobileMenu();

    // Table search filtering
    const searchInputs = document.querySelectorAll('[data-filter-table]');
    searchInputs.forEach(input => {
        input.addEventListener('keyup', (e) => {
            const tableId = e.target.getAttribute('data-filter-table');
            const table = document.getElementById(tableId);
            if (!table) return;
            
            const term = e.target.value.toLowerCase();
            const rows = table.querySelectorAll('tbody tr');
            
            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                if (text.includes(term)) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        });
    });
});
