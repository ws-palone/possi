INSERT INTO `Room` (`id`, `name`) VALUES (1, 'i50');
INSERT INTO `Room` (`id`,`name`) VALUES (2, 'i51');
INSERT INTO `Person` (`id`, `email`, `firstName`, `lastName`, `promo`, `role`, `uid`) VALUES (1, 'david.michel.2@etudiant.univ-rennes1.fr', 'DAVID', 'Michel', NULL, 0, '13006294'), (2, 'corentin.clement@etudiant.univ-rennes1.fr', 'CORENTIN', 'Clement', NULL, 0, '13008385'), (3, 'didier.certain@univ-rennes1.fr', 'DIDIER', 'Certain', NULL, 1, 'dcertain'), (4, 'mickael.foursov@univ-rennes1.fr', 'MICKAEL', 'Foursov', NULL, 1, 'foursov'), (5, 'islame.jebbari@etudiant.univ-rennes1.fr', 'ISLAME', 'Jebbari', NULL, 0, '15011509'), (6, 'sebastien.ferre@univ-rennes1.fr', 'SEBASTIEN', 'Ferre', NULL, 1, 'sferre'), (7, 'pauline.leverge@etudiant.univ-rennes1.fr', 'PAULINE', 'Le Verge', NULL, 0, '11001295'), (8, 'alexandre.lecut@etudiant.univ-rennes1.fr', 'ALEXANDRE', 'Lecut', NULL, 0, '11008880'), (9, 'maxime.bourdel@etudiant.univ-rennes1.fr', 'MAXIME', 'Bourdel', NULL, 0, '13001274'), (10, 'Gilles.Lesventes@univ-rennes1.fr', 'GILLES', 'Lesventes', NULL, 1, 'lesvente');
INSERT INTO `Planning` (`id`, `dayPeriod_from`, `dayPeriod_to`, `lunchBreak_from`, `lunchBreak_to`, `name`,`nbMaxOralDefensePerDay`,`oralDefenseDuration`, `oralDefenseInterlude`, `period_from`, `period_to`, `admin_id`) VALUES (1, '2015-02-04 08:00:00', '2015-02-04 18:15:00', '2015-02-04 12:15:00', '2015-02-04 14:00:00', 'M2 MIAGE VET', NULL, 20, NULL, '2015-02-04 00:00:00', '2015-02-12 00:00:00', 3);
INSERT INTO `Participant` (`id`, `followingTeacher_id`, `student_id`) VALUES (1, 3, 2), (2, 4, 1), (3, 6, 5), (4, 4, 7), (5, 3, 8), (6, 10, 9);
INSERT INTO `Planning_Participant` (`Planning_id`, `participants_id`) VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6);
INSERT INTO `Priority` (`id`, `role`, `weight`) VALUES (1, 0, 1), (2, 1, 3);
INSERT INTO `Planning_Priority` (`Planning_id`, `priorities_id`) VALUES ('1', '1'), ('1', '2');
INSERT INTO `Planning_Room` (`Planning_id`, `rooms_id`) VALUES ('1', '1'), ('1', '2');


