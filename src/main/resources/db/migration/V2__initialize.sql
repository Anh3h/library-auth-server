INSERT INTO `library` (`id`, `uuid`, `name`, `address`, `logo`, `enabled`) VALUES
(1, '5e95f5ca-48c5-4202-b168-32f7d31582fb', 'University of Buea Library', NULL, NULL, 1);

INSERT INTO `role` (`id`, `name`) VALUES
('53355714-6279-4dfc-b2ca-4b1af87c0a40', 'ROLE_ADMIN');

INSERT INTO `user` (`id`, `uuid`, `first_name`, `last_name`, `email`, `password`, `dob`, `telephone`, `address`, `library_id`, `enabled`, `account_locked`) VALUES
(1, '0025e086-a2ef-4332-8a39-468406f9d2b8', 'Admin', 'Admin', 'admin@gmail.com', '$2a$10$bA3g4SjUcIkGxSZ4BpjmdO58Zb6k1.BxrrIbMK/LQL121dH5SBySi', NULL, NULL, NULL, 1, 1, 0);

INSERT INTO `user_has_role` (`user_id`, `role_id`) VALUES
(1, '53355714-6279-4dfc-b2ca-4b1af87c0a40');