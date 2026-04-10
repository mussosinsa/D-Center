Ext.define('Flamingo2.view.dashboard.DataCenterDashboard', {
    extend: 'Flamingo2.panel.Panel',

    title: '대시보드',
    iconCls: 'fa fa-tachometer',
    cls: 'dc-dashboard',
    bodyPadding: 12,
    scrollable: true,

    items: [
        {
            xtype: 'component',
            cls: 'dc-dashboard-title',
            html: '대시보드'
        },
        {
            xtype: 'container',
            cls: 'dc-kpi-wrap',
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            defaults: {
                xtype: 'component',
                cls: 'dc-kpi-card',
                margin: '0 10 0 0',
                flex: 1
            },
            items: [
                {html: '<div class="dc-kpi-value">0<span>/0</span></div><div class="dc-kpi-label">노드</div>'},
                {html: '<div class="dc-kpi-value">0<span>/0</span></div><div class="dc-kpi-label">가상 머신</div>'},
                {html: '<div class="dc-kpi-value">0<span>/0</span></div><div class="dc-kpi-label">VM 풀</div>'},
                {html: '<div class="dc-kpi-value">0<span>코어</span></div><div class="dc-kpi-label">전체 CPU</div>'},
                {html: '<div class="dc-kpi-value">0<span>GB</span></div><div class="dc-kpi-label">전체 메모리</div>'},
                {html: '<div class="dc-kpi-value">0<span>GB</span></div><div class="dc-kpi-label">전체 스토리지</div>'},
                {html: '<div class="dc-kpi-value">0</div><div class="dc-kpi-label">클러스터</div>'},
                {html: '<div class="dc-kpi-value">0</div><div class="dc-kpi-label">로드 밸런서</div>'},
                {margin: 0, html: '<div class="dc-kpi-value">0</div><div class="dc-kpi-label">배포 앱</div>'}
            ]
        },
        {
            xtype: 'container',
            margin: '12 0 0 0',
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            defaults: {
                xtype: 'component',
                cls: 'dc-panel',
                margin: '0 12 0 0',
                flex: 1
            },
            items: [
                {
                    html: '<div class="dc-panel-title">가상 머신 상태</div>' +
                        '<div class="dc-ring"><div><strong>0%</strong><span>실행 중</span></div></div>' +
                        '<div class="dc-empty">데이터 없음</div>' +
                        '<div class="dc-badges"><span class="run">실행 중 0</span><span class="stop">중지됨 0</span></div>'
                },
                {
                    html: '<div class="dc-panel-title">노드 상태</div>' +
                        '<div class="dc-ring"><div><strong>0%</strong><span>Ready</span></div></div>' +
                        '<div class="dc-empty">데이터 없음</div>' +
                        '<div class="dc-badges"><span class="ready">Ready 0</span><span class="not-ready">NotReady 0</span></div>'
                },
                {
                    html: '<div class="dc-panel-title">VM 풀 상태</div>' +
                        '<div class="dc-ring"><div><strong>0%</strong><span>실행 중</span></div></div>' +
                        '<div class="dc-empty">데이터 없음</div>' +
                        '<div class="dc-badges"><span class="run">실행 중 0</span><span class="stop2">중지됨 0</span></div>'
                },
                {
                    margin: 0,
                    flex: 0.9,
                    html: '<div class="dc-panel-title">리소스 요약</div>' +
                        '<ul class="dc-summary">' +
                        '<li><span>데이터 볼륨</span><em>0</em></li>' +
                        '<li><span>네트워크</span><em>0</em></li>' +
                        '<li><span>자동 확장</span><em>0</em></li>' +
                        '<li><span>인스턴스 유형</span><em>0</em></li>' +
                        '<li><span>클러스터</span><em>0</em></li>' +
                        '<li><span>로드 밸런서</span><em>0</em></li>' +
                        '</ul>'
                }
            ]
        }
    ]
});
