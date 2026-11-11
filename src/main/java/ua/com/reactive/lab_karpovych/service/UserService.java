package ua.com.reactive.lab_karpovych.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lab_karpovych.entity.RolesHasUsers;
import ua.com.reactive.lab_karpovych.entity.User;
import ua.com.reactive.lab_karpovych.repository.RoleRepository;
import ua.com.reactive.lab_karpovych.repository.RolesHasUsersRepository;
import ua.com.reactive.lab_karpovych.repository.UserRepository;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;
    private final RolesHasUsersRepository rolesHasUsersRepository;
    private final RoleRepository roleRepository;

    public Flux<GrantedAuthority> getAuthorities(Long userId) {
        return rolesHasUsersRepository.findRoleIdsByUserId(userId)
                .flatMap(roleId -> roleRepository.findById(roleId))
                .map(role -> role).cast(GrantedAuthority.class);
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(user ->
                        getAuthorities(user.getId())
                                .collectList()
                                .map(authorities -> {
                                    user.setAuthorities(new HashSet<>(authorities));
                                    return user;
                                })
                );
    }

    public Mono<Boolean> register(String username, String password) {
        return userRepository.findByUsername(username)
                .map(existing -> false)
                .switchIfEmpty(Mono.defer(() ->
                        userRepository.save(new User(null, username, password))
                                .flatMap(saved -> roleRepository.findByName("ROLE_User")
                                        .flatMap(role -> rolesHasUsersRepository
                                                .save(new RolesHasUsers(null, role.getId(), saved.getId()))))
                                .thenReturn(true)));
    }
}