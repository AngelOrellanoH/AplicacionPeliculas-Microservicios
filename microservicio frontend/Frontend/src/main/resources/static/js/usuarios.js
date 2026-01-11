document.addEventListener('DOMContentLoaded', () => {

    // Botón editar
    document.querySelectorAll('.btn-editar').forEach(btn => {
        btn.addEventListener('click', () => {
            const username = btn.getAttribute('data-username');

            const divInfo = document.querySelector(`.usuario-info[data-username="${username}"]`);
            const formEdit = document.querySelector(`.form-edicion[data-username="${username}"]`);


            if (divInfo && formEdit) {
                divInfo.classList.add("hidden");
                formEdit.classList.remove("hidden");
            }
        });
    });

    // Botón cancelar
    document.querySelectorAll('.btn-cancelar').forEach(btn => {
        btn.addEventListener('click', () => {
            const username = btn.getAttribute('data-username');

            const divInfo = document.querySelector(`.usuario-info[data-username="${username}"]`);
            const formEdit = document.querySelector(`.form-edicion[data-username="${username}"]`);

            if (divInfo && formEdit) {
                divInfo.classList.remove("hidden");
                formEdit.classList.add("hidden")
            }
        });
    });

    // Confirmar eliminación
    document.querySelectorAll('.btn-eliminar').forEach(boton => {
        boton.addEventListener('click', function (event) {
            const confirmado = confirm("¿Estás seguro de que deseas eliminar este usuario?");
            if (!confirmado) {
                event.preventDefault();
            }
        });
    });

    // Ocultar alertas automáticamente
    const alertaError = document.querySelector(".alert-error");
    const alertaExito = document.querySelector(".alert-success");

    [alertaError, alertaExito].forEach(alerta => {
        if (alerta) {
            setTimeout(() => {
                alerta.style.opacity = "0";
                setTimeout(() => alerta.remove(), 500);
            }, 4000);
        }
    });
});
