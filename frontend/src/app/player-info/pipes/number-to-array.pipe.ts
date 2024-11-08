import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'numberToArray'
})
export class NumberToArrayPipe implements PipeTransform {

  transform(value: number): number[] {
    return Array(value).fill(value).map((_x, i) => i);
  }

}
