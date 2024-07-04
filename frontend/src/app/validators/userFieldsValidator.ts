import { AbstractControl } from '@angular/forms';
import { map } from 'rxjs/operators';
import { UsersService } from '../services/user/users.service';

export function uniqueValueValidator(userService: UsersService, signup:boolean) {
    return (control: AbstractControl) => {
        return userService.checkIfValueExists(control.value, signup).pipe(
            map(exists => exists ? { uniqueValue: true } : null)
        );
    };
}