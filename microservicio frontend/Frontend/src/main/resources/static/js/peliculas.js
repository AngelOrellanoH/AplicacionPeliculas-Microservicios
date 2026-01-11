document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById("modal");
    const form = modal.querySelector("form");

    const inputId = document.getElementById("peliculaId");
    const inputTitulo = document.getElementById("titulo");
    const inputAnio = document.getElementById("anio");
    const inputDuracion = document.getElementById("duracion");
    const inputPais = document.getElementById("pais");
    const inputDireccion = document.getElementById("direccion");
    const inputGenero = document.getElementById("genero");
    const inputSinopsis = document.getElementById("sinopsis");
    const inputImagen = document.getElementById("imagenPortada");

    const hiddenInput = document.getElementById("actores-hidden");
    const select = document.getElementById("actores-select");
    const tagsContainer = document.getElementById("tags-container");

    const tituloModal = modal.querySelector(".modal-header h2");
    const btnCrear = modal.querySelector(".btn-crear");

    const selectedValues = new Set();
    const removedOptions = new Map();

    function updateHiddenInput() {
        hiddenInput.value = Array.from(selectedValues).join(",");
    }

    // Abrir modal para nueva película
    document.querySelector(".btn-agregar").addEventListener("click", () => {
        form.reset();
        inputId.value = "";
        tagsContainer.innerHTML = "";
        selectedValues.clear();
        removedOptions.clear();
        hiddenInput.value = "";
        tituloModal.textContent = "Nueva Película";
        btnCrear.textContent = "Crear";
        modal.classList.remove("hidden");
    });

    // Cerrar modal
    document.querySelector(".modal-close").addEventListener("click", () => modal.classList.add("hidden"));
    document.querySelector(".btn-cancelar").addEventListener("click", () => modal.classList.add("hidden"));

    // Editar película
    document.querySelectorAll(".btn-editar").forEach(btn => {
        btn.addEventListener("click", () => {
            const { id, titulo, anio, duracion, pais, direccion, genero, sinopsis, imagen } = btn.dataset;


            inputId.value = id;
            inputTitulo.value = titulo;
            inputAnio.value = anio;
            inputDuracion.value = duracion;
            inputPais.value = pais;
            inputDireccion.value = direccion;
            inputGenero.value = genero;
            inputSinopsis.value = sinopsis;
            inputImagen.value = imagen;


            updateHiddenInput();

            tituloModal.textContent = "Editar Película";
            btnCrear.textContent = "Actualizar";
            modal.classList.remove("hidden");
        });
    });

    // Enviar formulario
    form.addEventListener("submit", (event) => {
        event.preventDefault();

        const isEditing = inputId.value !== "";
        const actionUrl = isEditing ? `/pelicula/editar/${inputId.value}` : `/pelicula`;

        const tempForm = document.createElement("form");
        tempForm.method = "POST";
        tempForm.action = actionUrl;

        const campos = [
            { name: "titulo", value: inputTitulo.value },
            { name: "anio", value: inputAnio.value },
            { name: "duracion", value: inputDuracion.value },
            { name: "pais", value: inputPais.value },
            { name: "direccion", value: inputDireccion.value },
            { name: "genero", value: inputGenero.value },
            { name: "sinopsis", value: inputSinopsis.value },
            { name: "imagenPortada", value: inputImagen.value }
        ];

        campos.forEach(campo => {
            const input = document.createElement("input");
            input.type = "hidden";
            input.name = campo.name;
            input.value = campo.value;
            tempForm.appendChild(input);
        });

        Array.from(selectedValues).forEach(id => {
            const input = document.createElement("input");
            input.type = "hidden";
            input.name = "actoresIds";
            input.value = id;
            tempForm.appendChild(input);
        });

        document.body.appendChild(tempForm);
        tempForm.submit();
    });

    // Confirmar eliminación
    document.querySelectorAll('.btn-delete').forEach(btn => {
        btn.addEventListener('click', e => {
            if (!confirm("¿Estás seguro de que deseas eliminar esta película?")) {
                e.preventDefault();
            }
        });
    });

    // Agregar actores como tags desde el selector
    select.addEventListener("change", () => {
        const selectedOption = select.options[select.selectedIndex];
        const id = selectedOption.value;
        const name = selectedOption.text;

        if (!id || selectedValues.has(id)) return;

        selectedValues.add(id);

        const tag = document.createElement("div");
        tag.classList.add("tag");
        tag.setAttribute("data-id", id);
        tag.innerHTML = `${name}<button type="button">&times;</button>`;

        tag.querySelector("button").addEventListener("click", () => {
            tag.remove();
            selectedValues.delete(id);
            updateHiddenInput();
            if (removedOptions.has(id)) {
                select.appendChild(removedOptions.get(id));
                removedOptions.delete(id);
            }
        });

        tagsContainer.appendChild(tag);
        updateHiddenInput();
        removedOptions.set(id, selectedOption);
        select.removeChild(selectedOption);
        select.selectedIndex = 0;
    });

    // Alertas
    const alertaError = document.querySelector(".alert:not(.alert-success)");
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
