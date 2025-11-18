const styles = getComputedStyle(document.documentElement);

const color_red_bright = styles.getPropertyValue("--color-bright-red").trim();
const color_red = styles.getPropertyValue("--color-red").trim();
const color_light = styles.getPropertyValue("--color-light").trim();
const color_gray = styles.getPropertyValue("--color-gray").trim();
const color_darker = styles.getPropertyValue("--color-darker").trim();
const color_dark = styles.getPropertyValue("--color-darks").trim();

// Инициализация глобальных переменных
if (typeof globalX === 'undefined') window.globalX = null;
if (typeof globalY === 'undefined') window.globalY = null;
if (typeof globalR === 'undefined') window.globalR = null;
if (typeof hits === 'undefined') window.hits = [];

function updateGlobalValues(inX, inY, inR) {
    document.dispatchEvent(
        new CustomEvent('updateGlobalValues', {
            detail: {
                r: Number(inR),
                x: Number(inX),
                y: Number(inY)
            }
        })
    );
}

function updateHits(inHits) {
    console.log('Updating hits:', inHits);
    document.dispatchEvent(
        new CustomEvent('updateHits', {
            detail: {
                hits: inHits
            }
        })
    );
}

function handleUpdateFromServer(xhr, status, args) {
    // Проверяем, что данные пришли
    if (args.newX !== undefined || args.newY !== undefined || args.newR !== undefined) {
        // Отправляем кастомное событие, которое ждет canvas.js
        document.dispatchEvent(new CustomEvent('updateGlobalValues', {
            detail: {
                x: args.newX, // ОДНО ЧИСЛО
                y: args.newY, // ОДНО ЧИСЛО
                r: args.newR  // ОДНО ЧИСЛО
            }
        }));
    }
    
    // Также обновляем hits если они пришли
    if (args.hits !== undefined) {
        updateHits(args.hits);
    }
}