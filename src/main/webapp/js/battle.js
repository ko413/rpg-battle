// 戦闘画面の入力補助（なくても操作できるよう、最低限の機能だけ）
(function () {
    // 1. 戦闘ログを最新の行までスクロール（上にスクロールすると履歴が読める）
    var log = document.getElementById('log');
    if (log) {
        log.scrollTop = log.scrollHeight;
    }

    // 2. 「変身」「守る」など対象を選ばない行動のときは、敵の選択を無効にする
    var enemyPanel = document.getElementById('enemy-panel');
    var targetInputs = document.querySelectorAll('input[name="targetIndex"]');
    var actions = document.querySelectorAll('input[name="actionChoice"][data-target]');

    function updateTargets() {
        var needsTarget = true;
        actions.forEach(function (a) {
            if (a.checked) { needsTarget = a.dataset.target === 'true'; }
        });
        targetInputs.forEach(function (t) { t.disabled = !needsTarget; }); // 無効なら送信もされない
        if (enemyPanel) { enemyPanel.classList.toggle('no-target', !needsTarget); }
    }

    actions.forEach(function (a) { a.addEventListener('change', updateTargets); });
    updateTargets();

    // 3. 二重送信の防止
    var form = document.getElementById('action-form');
    if (form) {
        form.addEventListener('submit', function () {
            var button = form.querySelector('button[type="submit"]');
            if (button) { button.disabled = true; }
        });
    }
})();
