document.addEventListener('DOMContentLoaded', () => {

    // Confirmar eliminación
    document.querySelectorAll('.btn-eliminar').forEach(boton => {
        boton.addEventListener('click', function (event) {
            const confirmado = confirm("¿Estás seguro de que deseas eliminar esta crítica?");
            if (!confirmado) {
                event.preventDefault();
            }
        });
    });

    const alertaError = document.querySelector(".alert:not(.alert-success)");
    const alertaExito = document.querySelector(".alert-success");

    if (alertaError) {
        setTimeout(() => {
            alertaError.style.transition = "opacity 0.5s ease";
            alertaError.style.opacity = "0";
            setTimeout(() => alertaError.remove(), 500);
        }, 4000);
    }

    if (alertaExito) {
        setTimeout(() => {
            alertaExito.style.transition = "opacity 0.5s ease";
            alertaExito.style.opacity = "0";
            setTimeout(() => alertaExito.remove(), 500);
        }, 4000);
    }
});