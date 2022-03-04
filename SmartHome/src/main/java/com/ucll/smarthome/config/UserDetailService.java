package com.ucll.smarthome.config;


import com.ucll.smarthome.dto.UserLoginDTO;
import com.ucll.smarthome.persistence.entities.User;
import com.ucll.smarthome.persistence.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("UserDetailService")
public class UserDetailService implements UserDetailsService {

    private final UserDAO userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public UserDetailService(UserDAO userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findFirstByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found",s)));
        return new UserPrincipal(user);
    }

    public UserDetails Login(UserLoginDTO userLoginDTO) throws Exception {
        UserDetails user = loadUserByUsername(userLoginDTO.getUsername());
        if(!passwordEncoder.matches(userLoginDTO.getPassword(),user.getPassword())){
            throw new Exception(String.format("User %s not found",userLoginDTO.getUsername()));
        }
        return user;
    }

}
