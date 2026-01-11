document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById("modal");
    const form = modal.querySelector("form");

    const inputNombre = document.getElementById("nombre");
    const inputFecha = document.getElementById("fechaNacimiento");
    const inputPais = document.getElementById("paisNacimiento");
    const inputId = document.getElementById("actorId");

    const tituloModal = modal.querySelector(".modal-header h2");
    const btnCrear = modal.querySelector(".btn-crear");

    // Abrir modal para nuevo actor
    document.querySelector(".btn-agregar").addEventListener("click", () => {
        form.reset();
        inputId.value = ""; // borrar ID
        tituloModal.textContent = "Nuevo Actor";
        btnCrear.textContent = "Crear";
        modal.classList.remove("hidden");
    });

    // Cerrar modal
    document.querySelector(".modal-close").addEventListener("click", () => {
        modal.classList.add("hidden");
    });
    document.querySelector(".btn-cancelar").addEventListener("click", () => {
        modal.classList.add("hidden");
    });

    // Abrir modal para editar actor
    document.querySelectorAll(".btn-editar").forEach(btn => {
        btn.addEventListener("click", () => {
            const { nombre, fecha, pais, id } = btn.dataset;

            inputNombre.value = nombre;
            inputFecha.value = fecha;
            inputPais.value = pais;
            inputId.value = id;

            tituloModal.textContent = "Editar Actor";
            btnCrear.textContent = "Actualizar";
            modal.classList.remove("hidden");
        });
    });

    // Manejar envío del formulario
    form.addEventListener("submit", function (event) {
        event.preventDefault();

        const isEditing = inputId.value !== "";
        const actionUrl = isEditing
            ? `/actor/editar/${inputId.value}`
            : `/actor`;

        // Crear formulario dinámico para enviar como POST
        const tempForm = document.createElement("form");
        tempForm.method = "POST";
        tempForm.action = actionUrl;

        const campos = [
            { name: "nombre", value: inputNombre.value },
            { name: "fechaNacimiento", value: inputFecha.value },
            { name: "paisNacimiento", value: inputPais.value }
        ];

        if (isEditing) {
            campos.push({ name: "id", value: inputId.value });
        }

        campos.forEach(campo => {
            const input = document.createElement("input");
            input.type = "hidden";
            input.name = campo.name;
            input.value = campo.value;
            tempForm.appendChild(input);
        });

        document.body.appendChild(tempForm);
        tempForm.submit();
    });

    // Alertas automáticas
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

    // Confirmar eliminación
    document.querySelectorAll('.btn-delete').forEach(boton => {
        boton.addEventListener('click', function (event) {
            const confirmado = confirm("¿Estás seguro de que deseas eliminar este actor?");
            if (!confirmado) {
                event.preventDefault();
            }
        });
    });
});
