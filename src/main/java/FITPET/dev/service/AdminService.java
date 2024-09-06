package FITPET.dev.service;

import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.common.security.jwt.JwtTokenProvider;
import FITPET.dev.common.status.ErrorStatus;
import FITPET.dev.converter.AdminConverter;
import FITPET.dev.dto.request.AdminRequest;
import FITPET.dev.dto.response.AdminResponse;
import FITPET.dev.entity.Admin;
import FITPET.dev.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    /*
     * ADMIN 마스터 계정 회원가입
     * @param signUpDto
     * @return
     */
    @Transactional
    public AdminResponse.SignDto signUp(AdminRequest.SignUpDto signUpDto) {
        findExistingAdmin(signUpDto.getId());

        // id = admin, pwd = admin인 계정만 회원가입 허용
        if (!signUpDto.getId().equals("admin") || !signUpDto.getPassword().equals("admin"))
            throw new GeneralException(ErrorStatus.INVALID_ADMIN_SIGN_UP);

        // create and save admin
        String password = passwordEncoder.encode(signUpDto.getPassword());
        Admin admin = AdminConverter.toAdmin(signUpDto.getId(), password, signUpDto.getAdminName());
        adminRepository.save(admin);

        // create token
        String accessToken = jwtTokenProvider.createAccessToken(admin.getAdminId());
        String refreshToken = jwtTokenProvider.createRefreshToken(admin.getAdminId());

        // save refresh token
        admin.setRefreshToken(refreshToken);
        return AdminConverter.toSignDto(accessToken, refreshToken);
    }

    /*
     * ADMIN 계정 로그인
     * @param signInDto
     * @return
     */
    public AdminResponse.SignDto signIn(AdminRequest.SignInDto signInDto) {
        Admin admin = findAdminById(signInDto.getId());

        if (!passwordEncoder.matches(signInDto.getPassword(), admin.getPassword()))
            throw new GeneralException(ErrorStatus.INVALID_PASSWORD_MATCH);

        // create token
        String accessToken = jwtTokenProvider.createAccessToken(admin.getAdminId());
        String refreshToken = jwtTokenProvider.createRefreshToken(admin.getAdminId());

        // save refresh token
        admin.setRefreshToken(refreshToken);
        return AdminConverter.toSignDto(accessToken, refreshToken);
    }

    private void findExistingAdmin(String id) {
        Optional<Admin> existingUser = getOptionalAdminById(id);
        if (existingUser.isPresent())
            throw new GeneralException(ErrorStatus.ALREADY_EXIST_ADMIN_ACCOUNT);
    }

    private Admin findAdminById(String id){
        return getOptionalAdminById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_ADMIN));
    }

    private Optional<Admin> getOptionalAdminById(String id){
        return adminRepository.findById(id);
    }

}
