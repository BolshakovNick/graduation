package ru.bolshakov.internship.dishes_rating.controller.restaurant;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bolshakov.internship.dishes_rating.controller.AuthenticationUtil;
import ru.bolshakov.internship.dishes_rating.dto.vote.VoteDTO;
import ru.bolshakov.internship.dishes_rating.service.VoteService;

@RestController
@RequestMapping("/restaurants")
public class RestaurantVotingController {

    private final VoteService voteService;

    public RestaurantVotingController(VoteService voteService) {
        this.voteService = voteService;
    }

    @ApiOperation(value = "Allows user to vote for the restaurant. Check if user already vote today and allows to change his vote if current time less than boundary time (11:00). Else forbids to change vote.")
    @ApiResponses
            ({
                    @ApiResponse(code = 200, message = "Voting was successful. Return information about voting (id - ID of new vote; localTime - time of voting; userId - user, who voted; restaurantId - for which user voted."),
                    @ApiResponse(code = 400, message = "Changing vote is unavailable yet"),
                    @ApiResponse(code = 404, message = "Restaurant with such ID not found")})
    @PostMapping("/{restaurantId}/vote")
    public ResponseEntity<VoteDTO> vote(@PathVariable @Parameter(in = ParameterIn.PATH, description = "ID of restaurant for which user votes") Long restaurantId,
                                        Authentication authentication) {
        return ResponseEntity.ok(voteService.vote(AuthenticationUtil.getAuthUserId(authentication), restaurantId));
    }
}