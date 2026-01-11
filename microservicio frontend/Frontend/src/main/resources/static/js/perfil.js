function activarEdicion(id) {
    const contenedor = document.querySelector(`[data-critica-id="${id}"]`);
    if (!contenedor) return;

    const formEdicion = contenedor.querySelector('.form-edicion');
    const valoracionText = contenedor.querySelector('.valoracion-text');

    formEdicion.classList.remove("hidden");
    valoracionText.style.display = "none";

}

function cancelarEdicion(id) {
    const contenedor = document.querySelector(`[data-critica-id="${id}"]`);
    if (!contenedor) return;

    const formEdicion = contenedor.querySelector('.form-edicion');
    const valoracionText = contenedor.querySelector('.valoracion-text');

    formEdicion.classList.add("hidden");
    valoracionText.style.display = "block";
}

document.addEventListener('DOMContentLoaded', () => {
    // Botón editar
    document.querySelectorAll('.btn-editar').forEach(btn => {
        btn.addEventListener('click', () => {
            const id = btn.dataset.id;
            activarEdicion(id);
        });
    });

    // Botón cancelar
    document.querySelectorAll('.btn-cancelar').forEach(btn => {
        btn.addEventListener('click', () => {
            const id = btn.dataset.id;
            cancelarEdicion(id);
        });
    });

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
