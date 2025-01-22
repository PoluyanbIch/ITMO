// Populate the sidebar
//
// This is a script, and not included directly in the page, to control the total size of the book.
// The TOC contains an entry for each page, so if each page includes a copy of the TOC,
// the total size of the page becomes O(n**2).
class MDBookSidebarScrollbox extends HTMLElement {
    constructor() {
        super();
    }
    connectedCallback() {
        this.innerHTML = '<ol class="chapter"><li class="chapter-item expanded "><a href="Karpov/2_integers/1_division.html"><strong aria-hidden="true">1.</strong> Chapter 1</a></li><li class="chapter-item expanded "><div><strong aria-hidden="true">2.</strong> Линейные пространства</div></li><li><ol class="section"><li class="chapter-item expanded "><a href="Karpov/5_lin_space/1.html"><strong aria-hidden="true">2.1.</strong> Линейное пространство. Свойства.</a></li><li class="chapter-item expanded "><a href="Karpov/5_lin_space/2.html"><strong aria-hidden="true">2.2.</strong> Линейное подпространство.</a></li><li class="chapter-item expanded "><a href="Karpov/5_lin_space/3.html"><strong aria-hidden="true">2.3.</strong> Линейная комбинация, линейная оболочка. Порождающая система векторов.</a></li><li class="chapter-item expanded "><a href="Karpov/5_lin_space/4.html"><strong aria-hidden="true">2.4.</strong> Линейно зависимые и линейно независимые системы векторов и их свойства.</a></li><li class="chapter-item expanded "><a href="Karpov/5_lin_space/5.html"><strong aria-hidden="true">2.5.</strong> Однородные системы линейных уравнений: приведение к ступенчатому виду, нетривиальное решение.</a></li><li class="chapter-item expanded "><a href="Karpov/5_lin_space/6.html"><strong aria-hidden="true">2.6.</strong> Лемма о линейной зависимости линейных комбинаций.</a></li><li class="chapter-item expanded "><a href="Karpov/5_lin_space/7.html"><strong aria-hidden="true">2.7.</strong> Базис, размерность. Корректность определения размерности. Разложение по базису.</a></li><li class="chapter-item expanded "><a href="Karpov/5_lin_space/8.html"><strong aria-hidden="true">2.8.</strong> Существование базиса в конечно порожденном пространстве. Выделение базиса из конечной порождающей системы.</a></li><li class="chapter-item expanded "><a href="Karpov/5_lin_space/9.html"><strong aria-hidden="true">2.9.</strong> Дополнение до базиса линейно независимой системы в конечномерном пространстве.</a></li><li class="chapter-item expanded "><a href="Karpov/5_lin_space/10.html"><strong aria-hidden="true">2.10.</strong> Три эквивалентных определения базиса.</a></li><li class="chapter-item expanded "><a href="Karpov/5_lin_space/11.html"><strong aria-hidden="true">2.11.</strong> Сумма и пересечение линейных пространств.</a></li><li class="chapter-item expanded "><a href="Karpov/5_lin_space/12.html"><strong aria-hidden="true">2.12.</strong> Размерность суммы двух линейных пространств.</a></li><li class="chapter-item expanded "><a href="Karpov/5_lin_space/13.html"><strong aria-hidden="true">2.13.</strong> Прямая сумма. Свойство прямой суммы.</a></li><li class="chapter-item expanded "><a href="Karpov/5_lin_space/14.html"><strong aria-hidden="true">2.14.</strong> Критерий прямой суммы.</a></li><li class="chapter-item expanded "><a href="Karpov/5_lin_space/15.html"><strong aria-hidden="true">2.15.</strong> Размерность и базис прямой суммы конечного числа пространств.</a></li><li class="chapter-item expanded "><a href="Karpov/5_lin_space/16.html"><strong aria-hidden="true">2.16.</strong> Аффинные подпространства. Свойства.</a></li><li class="chapter-item expanded "><a href="Karpov/5_lin_space/17.html"><strong aria-hidden="true">2.17.</strong> Факторпространство и его размерность.</a></li></ol></li></ol>';
        // Set the current, active page, and reveal it if it's hidden
        let current_page = document.location.href.toString();
        if (current_page.endsWith("/")) {
            current_page += "index.html";
        }
        var links = Array.prototype.slice.call(this.querySelectorAll("a"));
        var l = links.length;
        for (var i = 0; i < l; ++i) {
            var link = links[i];
            var href = link.getAttribute("href");
            if (href && !href.startsWith("#") && !/^(?:[a-z+]+:)?\/\//.test(href)) {
                link.href = path_to_root + href;
            }
            // The "index" page is supposed to alias the first chapter in the book.
            if (link.href === current_page || (i === 0 && path_to_root === "" && current_page.endsWith("/index.html"))) {
                link.classList.add("active");
                var parent = link.parentElement;
                if (parent && parent.classList.contains("chapter-item")) {
                    parent.classList.add("expanded");
                }
                while (parent) {
                    if (parent.tagName === "LI" && parent.previousElementSibling) {
                        if (parent.previousElementSibling.classList.contains("chapter-item")) {
                            parent.previousElementSibling.classList.add("expanded");
                        }
                    }
                    parent = parent.parentElement;
                }
            }
        }
        // Track and set sidebar scroll position
        this.addEventListener('click', function(e) {
            if (e.target.tagName === 'A') {
                sessionStorage.setItem('sidebar-scroll', this.scrollTop);
            }
        }, { passive: true });
        var sidebarScrollTop = sessionStorage.getItem('sidebar-scroll');
        sessionStorage.removeItem('sidebar-scroll');
        if (sidebarScrollTop) {
            // preserve sidebar scroll position when navigating via links within sidebar
            this.scrollTop = sidebarScrollTop;
        } else {
            // scroll sidebar to current active section when navigating via "next/previous chapter" buttons
            var activeSection = document.querySelector('#sidebar .active');
            if (activeSection) {
                activeSection.scrollIntoView({ block: 'center' });
            }
        }
        // Toggle buttons
        var sidebarAnchorToggles = document.querySelectorAll('#sidebar a.toggle');
        function toggleSection(ev) {
            ev.currentTarget.parentElement.classList.toggle('expanded');
        }
        Array.from(sidebarAnchorToggles).forEach(function (el) {
            el.addEventListener('click', toggleSection);
        });
    }
}
window.customElements.define("mdbook-sidebar-scrollbox", MDBookSidebarScrollbox);
