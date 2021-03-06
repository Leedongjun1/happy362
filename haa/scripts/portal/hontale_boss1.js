/*
 * 퓨어온라인 소스 스크립트 입니다.
 * 
 * 포탈위치 : 시험의 동굴 1
 * 포탈설명 : 왼쪽 머리 소환
 * 
 * 제작 : 주크블랙
 * 
 */
importPackage(Packages.server.life);
function enter(pi) {
    //2408003리액터 위치에 8810024, 8810128 소환
    var map = pi.getPlayer().getMap();
    if (map.getReactor(2408003).getState() <= 0) {
        var pos = map.getReactor(2408003).getPosition();
        var mob = null;
        if (isChaos(pi) == 0) {
            mob = MapleLifeProvider.getMonster(8810024);
        } else {
            mob = MapleLifeProvider.getMonster(8810128);
        }
        pi.getPlayer().getMap().spawnMonsterOnGroundBelow(mob, pos);
        map.getReactor(2408003).setState(1);
    }
    return false;
}


function isChaos(pi) {
    return pi.getClient().getChannel() % 2;
}